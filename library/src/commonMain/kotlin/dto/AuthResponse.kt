package dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    val refreshToken: String,
    val userId: UInt,
    @SerialName("hoteId") val hostId: UInt
)
