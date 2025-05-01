package models

import dto.AuthResponse
import dto.TokenInfos
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Authentication informations
 *
 * @property username Account username
 * @property codes2p5 Establishment(s) codes
 * @property userId Account ID
 * @property roles Account roles
 * @property hostId Host ID, don't know what it is for exactly
 * @property accessToken Authorization token
 * @property refreshToken Authorization refresh token
 */
@Serializable
data class AuthInfos (
    val username: String,
    val password: String,
    val codes2p5: List<UInt>,
    val userId: UInt,
    val roles: List<String>,
    @SerialName("hoteId") val hostId: UInt,
    val accessToken: Token,
    val refreshToken: Token
) {
    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        fun decodeFromAuthResponse(authResponse: AuthResponse, password: String): AuthInfos {
            val accessTokenRaw = authResponse.accessToken

            val accessTokenInfosRaw = Base64
                .withPadding(Base64.PaddingOption.ABSENT)
                .decode(accessTokenRaw.split('.')[1])
                .decodeToString()

            val accessTokenInfos = Json.decodeFromString<TokenInfos>(accessTokenInfosRaw)

            val accessToken = Token(
                accessTokenRaw,
                Instant.fromEpochSeconds(accessTokenInfos.iat),
                Instant.fromEpochSeconds(accessTokenInfos.exp)
            )

            val refreshTokenRaw = authResponse.refreshToken

            val refreshTokenInfosRaw = Base64
                .withPadding(Base64.PaddingOption.ABSENT)
                .decode(refreshTokenRaw.split('.')[1])
                .decodeToString()

            val refreshTokenInfos = Json.decodeFromString<TokenInfos>(refreshTokenInfosRaw)

            val refreshToken = Token(
                refreshTokenRaw,
                Instant.fromEpochSeconds(refreshTokenInfos.iat),
                Instant.fromEpochSeconds(refreshTokenInfos.exp)
            )


            return AuthInfos(
                username = accessTokenInfos.username,
                password = password,
                codes2p5 = accessTokenInfos.codes2p5,
                userId = accessTokenInfos.userId,
                roles = accessTokenInfos.roles,
                hostId = accessTokenInfos.hostId,
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}
