package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: UInt,
    @SerialName("hoteId") val hostId: UInt
)
