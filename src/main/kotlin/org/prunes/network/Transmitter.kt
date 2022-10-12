package org.prunes.network

import com.google.gson.*
import org.prunes.json.WideData
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.net.Socket
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
                it.getOutputStream().write(command.toByteArray())
                it.close()
            }
        }.isSuccess

    fun sendTo(port: Int, data: WideData) = sendTo(port, gson.toJson(data))
}
