package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenInfos (
    val username: String,
    val codes2p5: List<UInt>,
    val userId: UInt,
    val roles: List<String>,
    @SerialName("hoteId") val hostId: UInt,
    val iat: Long,
    val exp: Long
)
