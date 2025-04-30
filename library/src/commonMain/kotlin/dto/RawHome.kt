package dto

import kotlinx.serialization.Serializable

@Serializable
data class RawHome(
    val latestPaiement: RawPayment?
)
