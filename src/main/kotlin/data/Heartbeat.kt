package data

import java.math.BigDecimal

data class Heartbeat(
    var entity: String? = null,
    var timestamp: BigDecimal? = null,
    var isWrite: Boolean? = null,
    var project: String? = null,
    var language: String? = null
)
