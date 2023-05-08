package org.prunes.network

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

val gson: Gson =
    GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
        override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(ZonedDateTime.of(src, ZoneId.of("Asia/Tokyo")).toString())
    }).registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
            ZonedDateTime.parse(json.asJsonPrimitive.asString).toLocalDateTime()
    }).create()
