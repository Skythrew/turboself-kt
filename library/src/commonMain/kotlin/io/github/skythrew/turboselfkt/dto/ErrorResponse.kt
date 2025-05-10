package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val statusCode: UInt,
    val message: String
)
