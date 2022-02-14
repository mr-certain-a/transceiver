package org.prunes.network

import com.google.gson.Gson
import kotlinx.coroutines.*
import org.prunes.json.WideData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import kotlin.coroutines.EmptyCoroutineContext

class Receiver(private val port: Int) {
    private val log: Logger = LoggerFactory.getLogger(javaClass.name)

    private lateinit var job: Job

    var isRunning = true

    fun listenJsonForJava(closure: Consumer<WideData>) {
        listen {
            closure.accept(Gson().fromJson(it, WideData::class.java))
        }
    }

    fun listenJson(closure: (WideData)->Unit) {
        listen {
            closure(Gson().fromJson(it, WideData::class.java))
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun listen(closure: (String)->Unit) {
        job = CoroutineScope(EmptyCoroutineContext).launch {
            log.info("start of command standby. [port=$port]")
            val listener = ServerSocket().apply { reuseAddress = true }
            listener.bind(InetSocketAddress(port))
            while(isRunning) {
                withContext(Dispatchers.IO) {
                    listener.accept().getInputStream().use {
                        when(val command = String(it.readBytes(), StandardCharsets.UTF_8)) {
                            "quit" -> Unit
                            "nop" -> Unit
                            else -> closure(command)
                        }
                    }
                }
            }
            listener.close()
            log.info("End of command standby. [port=$port]")
        }
    }

    fun close() {
        log.info("コマンド受付部品終了処理開始")
        isRunning = false
        Transmitter.sendTo(port, "quit")
        if(::job.isInitialized) {
            runBlocking {
                job.join()
            }
        }
        log.info("コマンド受付部品は正常に破棄されました。")
    }
}
