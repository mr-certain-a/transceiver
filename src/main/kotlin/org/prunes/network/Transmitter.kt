package org.prunes.network

import org.prunes.json.WideData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket

object Transmitter {
    var toHost = "localhost"

    fun sendTo(port: Int, command: String) =
        kotlin.runCatching {
            Socket().let {
                it.connect(InetSocketAddress(toHost, port))
                it.getOutputStream().write("$command${System.lineSeparator()}".toByteArray())
                it.close()
            }
        }.onFailure { throw it }.isSuccess

    fun sendWithResponse(port: Int, data: WideData) =
        sendWithResponse(port, gson.toJson(data))

    fun sendWithResponse(port: Int, command: String): WideData? =
        Socket().use {
            it.connect(InetSocketAddress(toHost, port))
            it.getOutputStream().use { os ->
                os.write("$command${System.lineSeparator()}".toByteArray())
                os.flush()

                it.getInputStream().use { ins ->
                    BufferedReader(InputStreamReader(ins)).use {br->
                        gson.fromJson(br.readLine(), WideData::class.java)
                    }
                }
            }
        }

    fun sendTo(port: Int, data: WideData) = sendTo(port, gson.toJson(data))

    fun response(sock: Socket, data: WideData) {
        response(sock, gson.toJson(data))
    }

    fun response(sock: Socket, command: String) {
        sock.getOutputStream().use { os ->
            os.write("$command${System.lineSeparator()}".toByteArray())
        }
    }
}
