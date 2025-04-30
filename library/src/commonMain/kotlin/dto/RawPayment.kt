package dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RawPayment(
    val id: UInt?,
    val montant: UInt,
    val statut: String,
    val token: String,
    val date: Instant
)
