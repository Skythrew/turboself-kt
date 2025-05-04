package dto

import kotlinx.serialization.Serializable

@Serializable
data class RawBookingDay(
    val dayReserv: Int,
    val reservDernSynchro: Int,
    val dayOfWeek: Int,
    val autorise: Boolean,
    val msg: String? = null,
    val dayLabel: String
)

@Serializable
data class RawBooking(
    val id: String,
    val annee: Int,
    val semaine: Int,
    val borne: RawTerminal,
    val joursAutorises: Int,
    val jours: List<RawBookingDay>
)

@Serializable
data class RawBookingsResult(
    val rsvWebDto: List<RawBooking>,
    val numSemaines: List<Int>,
    val isResaSoirActive: Boolean,
    val dateSemaine: String
)
