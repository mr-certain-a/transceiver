package org.prunes.network

import com.google.gson.*
import org.prunes.json.WideData
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object Transmitter {
    var gson: Gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
            override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext)
                    = JsonPrimitive(ZonedDateTime.of(src, ZoneId.of("Asia/Tokyo")).toString())
        }).create()

    fun sendTo(port: Int, command: String) =
        kotlin.runCatching {
            Socket().let {
                it.connect(InetSocketAddress("localhost", port))
                it.getOutputStream().write("$command${System.lineSeparator()}".toByteArray())
                it.close()
            }
        }.isSuccess

    fun sendWithResponse(port: Int, command: String): String? =
        runCatching {
            Socket().use {
                it.connect(InetSocketAddress("localhost", port))
                it.getOutputStream().use { os ->
                    os.write("$command${System.lineSeparator()}".toByteArray())
                    os.flush()

                    it.getInputStream().use { ins ->
                        BufferedReader(InputStreamReader(ins)).use {br->
                            br.readLine()
                        }
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

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
