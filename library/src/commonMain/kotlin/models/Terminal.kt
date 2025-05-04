package models

import dto.RawTerminal
import dto.RawTerminalPrice
import kotlinx.serialization.Serializable

@Serializable
data class TerminalPrice(
    val id: Int,
    val localId: Int,
    val name: String?,
    val price: Int
) {
    companion object {
        fun decodeFromRawTerminalPrice(rawTerminalPrice: RawTerminalPrice): TerminalPrice {
            return TerminalPrice(
                rawTerminalPrice.id,
                rawTerminalPrice.idOrig,
                rawTerminalPrice.lib,
                rawTerminalPrice.prix
            )
        }
    }
}

@Serializable
data class Terminal(
    val id: Int,
    val localId: Int,
    val code: Int,
    val name: String,
    val prices: List<TerminalPrice>
) {
    companion object {
        fun decodeFromRawTerminal(rawTerminal: RawTerminal): Terminal {
            return Terminal(
                rawTerminal.id,
                rawTerminal.idOrig,
                rawTerminal.code2p5,
                rawTerminal.lib,
                rawTerminal.prix.map {
                    rawTerminalPrice -> TerminalPrice.decodeFromRawTerminalPrice(rawTerminalPrice)
                }
            )
        }
    }
}
