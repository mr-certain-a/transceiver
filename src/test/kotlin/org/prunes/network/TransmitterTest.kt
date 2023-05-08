package org.prunes.network

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class TransmitterTest: Spek({
    describe("TransmitterTest") {
        context("send with response") {
            val port = 20202
            Receiver(port).let {
                it.listenCommand { sock, command ->
                    println("■received: $command")

                    Transmitter.response(sock, "{process: \"SOCKET RESPONSE\", masterFileName=\"NOTHING.PDF\"}")

                    println("■sent to client")
                }

                println("Transmitter.sendWithResponse")
                val res = Transmitter.sendWithResponse(port, "hello")
                println("返答:$res")
            }
        }
    }
})
