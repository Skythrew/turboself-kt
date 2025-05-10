package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class RawHome(
    val latestPaiement: RawPayment?
)
