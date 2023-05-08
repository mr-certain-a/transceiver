package org.prunes.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.prunes.json.WideData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.function.BiConsumer
import kotlin.coroutines.EmptyCoroutineContext

class Receiver(private val port: Int) {
    private val log: Logger = LoggerFactory.getLogger(javaClass.name)

    private lateinit var job: Job

    var isRunning = true

    fun listenJsonForJava(closure: BiConsumer<Socket, WideData>) {
        listenCommand { sock, str->
            closure.accept(sock, gson.fromJson(str, WideData::class.java))
        }
    }

    fun listenJson(closure: (Socket, WideData)->Unit) {
        listenCommand { sock, str->
            closure(sock, gson.fromJson(str, WideData::class.java))
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun listenCommand(closure: (Socket, String)->Unit) {
        job = CoroutineScope(EmptyCoroutineContext).launch {
            log.info("start of command standby. [port=$port]")
            val listener = ServerSocket().apply { reuseAddress = true }
            listener.bind(InetSocketAddress(port))
            while(isRunning) {
                listener.accept().let { sock ->
                    sock.getInputStream().use {
                        BufferedReader(InputStreamReader(it)).use {br ->
                            when(val command = br.readLine()) {
                                "quit" -> Unit
                                "nop" -> Unit
                                else -> closure(sock, command)
                            }
                        }
                    }
                }
            }
            listener.close()
            log.info("End of command standby. [port=$port]")
        }
    }

    @Deprecated("Add Argument. Replace `listenCommand`", ReplaceWith("listenCommand"))
    @Suppress("BlockingMethodInNonBlockingContext")
    fun listen(closure: (String)->Unit) {
        job = CoroutineScope(EmptyCoroutineContext).launch {
            log.info("start of command standby. [port=$port]")
            val listener = ServerSocket().apply { reuseAddress = true }
            listener.bind(InetSocketAddress(port))
            while(isRunning) {
                listener.accept().let { sock ->
                    sock.getInputStream().use {
                        when (val command = String(it.readBytes(), StandardCharsets.UTF_8)) {
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
