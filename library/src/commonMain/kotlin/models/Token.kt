package models

import dto.TokenInfos
import kotlinx.serialization.Serializable

/**
 * A JWT Token
 *
 * @property value The raw value of the token
 * @property infos Infos extracted from the token decode process
 * @property creationTimestamp The creation date of the token (under a Unix timestamp form)
 * @property expirationTimestamp The expiration date of the token (under a Unix timestamp form)
 */
@Serializable
data class Token(
    val value: String,
    val infos: TokenInfos,
    val creationTimestamp: UInt,
    val expirationTimestamp: UInt
)
