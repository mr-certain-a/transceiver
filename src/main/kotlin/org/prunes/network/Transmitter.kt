package org.prunes.network

import com.google.gson.Gson
import org.prunes.json.WideData
import java.net.InetSocketAddress
import java.net.Socket

class Transmitter {
    companion object {
        fun sendTo(port: Int, command: String) =
            kotlin.runCatching {
                Socket().let {
                    it.connect(InetSocketAddress("localhost", port))
                    it.getOutputStream().write(command.toByteArray())
                    it.close()
                }
            }.isSuccess

        fun sendTo(port: Int, data: WideData) = sendTo(port, Gson().toJson(data))
    }
}
