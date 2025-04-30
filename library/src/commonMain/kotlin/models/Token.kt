package models

import dto.TokenInfos
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val value: String,
    val infos: TokenInfos,
    val creationTimestamp: UInt,
    val expirationTimestamp: UInt
)
