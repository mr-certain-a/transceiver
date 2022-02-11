package org.prunes.json

import java.time.LocalDateTime
import java.util.*

data class WideData(
    val key: String = UUID.randomUUID().toString(),
    var requestCommand: String? = null,
    var masterFileName: String? = null,
    var categoryId: String? = null,
    var acceptDate: LocalDateTime? = null,
    var depId: String? = null,
    var depName: String? = null,
    var documentKey: String? = null,
    var documentKeySequence: String? = null,
    var documentId: String? = null,
    var documentTitle: String? = null,
    var mimeType: String? = null,
    var pid: String? = null,
    var registDate: LocalDateTime? = null,
    var rid: String? = null,
    var result: Boolean? = null,
)
