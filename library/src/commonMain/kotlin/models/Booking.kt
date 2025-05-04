package models

import dto.RawBooking
import dto.RawBookingDay
import getWeekRange
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.days

data class BookingDay(
    val booked: Boolean,
    val canBook: Boolean,
    val dayNumber: Int,
    val message: String,
    val reservations: Int,
    val date: Instant
) {
    companion object {
        fun decodeFromRawBookingDay(rawBookingDay: RawBookingDay, startOfWeek: Instant): BookingDay {
            return BookingDay(
                rawBookingDay.dayReserv > 0,
                rawBookingDay.autorise,
                rawBookingDay.dayOfWeek,
                rawBookingDay.msg ?: "",
                rawBookingDay.dayReserv,
                startOfWeek.plus((rawBookingDay.dayOfWeek - 1).days)
            )
        }
    }
}

data class Booking(
    val id: String,
    val week: Int,
    val from: Instant,
    val to: Instant,
    val terminal: Terminal,
    val days: List<BookingDay>
) {
    companion object {
        fun decodeFromRawBooking(rawBooking: RawBooking): Booking {
            val weekRange = getWeekRange(rawBooking.semaine, rawBooking.annee)
            return Booking(
                rawBooking.id,
                rawBooking.semaine,
                weekRange.from.toInstant(TimeZone.currentSystemDefault()),
                weekRange.to.toInstant(TimeZone.currentSystemDefault()),
                Terminal.decodeFromRawTerminal(rawBooking.borne),
                days = rawBooking.jours.map {
                    rawBookingDay -> BookingDay.decodeFromRawBookingDay(rawBookingDay, weekRange.from.toInstant(TimeZone.currentSystemDefault()))
                }
            )
        }
    }
}
