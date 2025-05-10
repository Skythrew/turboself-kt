package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class RawBalance(
    val id: UInt,
    val montantEstime: Int,
    val montantEstimeMsg: String,
    val montant: Int
)
