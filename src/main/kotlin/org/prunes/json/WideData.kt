package org.prunes.json

import java.time.LocalDateTime
import java.util.*

data class WideData(
    var key: String = UUID.randomUUID().toString(),
    var requestCommand: String? = null,

    var masterFileName: String? = null,
    var pid: String? = null,
    var rid: String? = null,
    var categoryId: String? = null,
    var acceptDate: LocalDateTime? = null,
    var registDate: LocalDateTime? = null,
    var depId: String? = null,
    var depName: String? = null,
    var documentKey: String? = null,
    var documentKeySequence: String? = null,
    var documentId: String? = null,
    var documentTitle: String? = null,
    var documentClass: String? = null,
    var mimeType: String? = null,
    var result: Boolean? = null,
    var ststime: LocalDateTime? = null,
    var atstime: LocalDateTime? = null,
    var expiration: LocalDateTime? = null,
    var directory: String? = null,
    var stampName: String? = null,
    var barcodeInfo: String? = null,

    var expansions0: String? = null,
    var expansions1: String? = null,
    var expansions2: String? = null,
    var expansions3: String? = null,
    var expansions4: String? = null,
    var expansions5: String? = null,
    var expansions6: String? = null,
    var expansions7: String? = null,
    var expansions8: String? = null,
    var expansions9: String? = null,
)
