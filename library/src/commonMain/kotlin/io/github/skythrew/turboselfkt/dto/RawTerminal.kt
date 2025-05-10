package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class RawTerminalPrice(
    val id: Int,
    val borneId: Int,
    val usage: Int,
    val prix: Int,
    val idOrig: Int,
    val lib: String? = null
)

@Serializable
data class RawTerminal(
    val id: Int,
    val code2p5: Int,
    val idOrig: Int,
    val lib: String,
    val prix: List<RawTerminalPrice>
)
