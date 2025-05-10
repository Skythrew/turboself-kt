package io.github.skythrew.turboselfkt.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RawHistoryEvent(
    val id: UInt,
    val date: Instant,
    val detail: String,
    val debit: Int? = null,
    val credit: Int? = null
)
