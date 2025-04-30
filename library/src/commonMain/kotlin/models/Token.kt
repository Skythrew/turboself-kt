package models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * A JWT Token
 *
 * @property value The raw value of the token
 * @property creationDate The creation date of the token
 * @property expirationDate The expiration date of the token
 */
@Serializable
data class Token(
    val value: String,
    val creationDate: Instant,
    val expirationDate: Instant
)
