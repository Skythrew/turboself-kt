package dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val statusCode: UInt,
    val message: String
)
