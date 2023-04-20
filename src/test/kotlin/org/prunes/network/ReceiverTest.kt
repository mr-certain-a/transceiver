package org.prunes.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.prunes.json.WideData
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDateTime
internal class ReceiverTest: Spek({
    describe("listen") {
        context("json send") {
            val port = 20202
            Receiver(port).let {
                it.listenJson { _, json ->
                    println("received: $json")
                }

                val data = WideData().apply {
                    registDate = LocalDateTime.now()
                    directory = "."
                }

                Transmitter.sendTo(port, data)

                runBlocking { delay(1000) }
                it.close()
            }
        }

        context("send and receive") {
            val port = 20202
            Receiver(port).let {
                it.listen { command ->
                    println("received: $command")
                }

                repeat(5) { i->
                    Transmitter.sendTo(port, "test $i")
                }

                runBlocking { delay(1000) }
                it.close()

                val res = Transmitter.sendTo(port, "error")
                println("送信結果:$res")
            }
        }
    }
})
