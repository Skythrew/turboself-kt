package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthOptions(
    val username: String,
    val password: String
)
