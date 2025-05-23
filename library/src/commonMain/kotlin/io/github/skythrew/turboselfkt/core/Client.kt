package io.github.skythrew.turboselfkt.core

import io.github.skythrew.turboselfkt.dto.AuthOptions
import io.github.skythrew.turboselfkt.dto.AuthResponse
import io.github.skythrew.turboselfkt.dto.RawBalance
import io.github.skythrew.turboselfkt.dto.RawBookResult
import io.github.skythrew.turboselfkt.dto.RawBookingsResult
import io.github.skythrew.turboselfkt.dto.RawEstablishment
import io.github.skythrew.turboselfkt.dto.RawHistoryEvent
import io.github.skythrew.turboselfkt.dto.RawHome
import io.github.skythrew.turboselfkt.dto.RawHost
import io.github.skythrew.turboselfkt.dto.RawPayment
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import io.github.skythrew.turboselfkt.models.AuthInfos
import io.github.skythrew.turboselfkt.models.Balance
import io.github.skythrew.turboselfkt.models.Booking
import io.github.skythrew.turboselfkt.models.BookingDay
import io.github.skythrew.turboselfkt.models.Establishment
import io.github.skythrew.turboselfkt.models.HistoryEvent
import io.github.skythrew.turboselfkt.models.Host
import io.github.skythrew.turboselfkt.models.Payment
import kotlin.time.Duration.Companion.minutes

/**
 * Turboself Client
 */
class TurboselfClient {
    private var authInfos: AuthInfos? = null
    private val apiManager = ApiManager()

    private fun getHostUrl(url: String): String {
        return url.replace(":hostId:", this.authInfos!!.hostId.toString())
    }

    /**
     * Log in with simple credentials.
     *
     * @param username Turboself account username (or email)
     * @param password Turboself account password
     *
     * @return Authentication informations
     */
    suspend fun loginWithCredentials(
        username: String,
        password: String,
    ): AuthInfos {
        val authRequest = this.apiManager.post(
            AUTH_LOGIN,
            AuthOptions(
                username,
                password
            )
        )

        val authResponse = Json.decodeFromString<AuthResponse>(authRequest.bodyAsText())

        this.apiManager.setAuthToken(authResponse.accessToken)

        val authInfos = AuthInfos.decodeFromAuthResponse(authResponse, password)

        this.authInfos = authInfos

        return authInfos
    }

    /**
     * Refresh authentication token
     *
     * Useful when storing a single client in an app storage
     * and using it multiple times.
     */
    suspend fun refreshToken() {
        if (this.authInfos == null)
            error("You haven't logged in a single time yet.")

        if (this.authInfos!!.accessToken.expirationDate < Clock.System.now() - 15.minutes)
            return

        this.loginWithCredentials(this.authInfos!!.username, this.authInfos!!.password)
    }

    /**
     * Get the available account balances.
     */
    suspend fun balances(): List<Balance> {
        val rawBalances = this.apiManager.getObj<List<RawBalance>>(getHostUrl(HOST_BALANCES))

        return rawBalances.map { rawBalance -> Balance.decodeFromRawBalance(rawBalance) }
    }

    /**
     * Get a specific Turboself payment.
     *
     * @param paymentToken Payment token
     */
    suspend fun payment(paymentToken: String): Payment {
        val rawPayment = this.apiManager.getObj<RawPayment>(getHostUrl(HOST_PAYMENT) + "/$paymentToken")

        return Payment.decodeFromRawPayment(this.authInfos!!.hostId, rawPayment)
    }

    /**
     * Get the latest payment.
     */
    suspend fun latestPayment(): Payment? {
        val rawHome = this.apiManager.getObj<RawHome>(getHostUrl(HOST_HOME))

        if (rawHome.latestPaiement == null)
            return null

        return Payment.decodeFromRawPayment(this.authInfos!!.hostId, rawHome.latestPaiement)
    }

    /**
     * Get the account history.
     */
    suspend fun history(): List<HistoryEvent> {
        val rawHistory = this.apiManager.getObj<List<RawHistoryEvent>>(getHostUrl(HOST_HISTORY))

        return HistoryEvent.decodeRawHistory(rawHistory)
    }

    /**
     * Get a specific establishment by ID.
     *
     * Provides a more complete information than getting information by 2p5.
     *
     * @param id Establishment ID
     */
    suspend fun establishmentByID(id: Int): Establishment {
        return Establishment
            .decodeFromRawEstablishment(
this
                    .apiManager
                    .getObj<RawEstablishment>(
                        ESTABLISHMENT_BY_ID + id.toString()
                    )
            )
    }

    /**
     * Get a specific establishment by 2p5 code.
     *
     * Only provides establishment name and Turboself version.
     *
     * @param code Establishment code
     */
    suspend fun establishmentBy2P5(code: Int): Establishment {
        return Establishment
            .decodeFromRawEstablishment(
                this
                    .apiManager
                    .getObj<List<RawEstablishment>>(
                        ESTABLISHMENT_BY_CODE + code
                    )[0]
            )
    }

    /**
     * Get host information.
     */
    suspend fun host(): Host {
        return Host
            .decodeFromRawHost(
                this.apiManager.getObj<RawHost>(getHostUrl(HOST)
                )
            )
    }

    /**
     * Get host siblings.
     */
    suspend fun siblings(): List<Host> {
        return this.apiManager.getObj<List<RawHost>>(getHostUrl(HOST_SIBLINGS)).map {
            rawHost -> Host.decodeFromRawHost(rawHost)
        }
    }

    /**
     * Get bookings for the specified week (or the current week if no
     * week is provided)
     *
     * @param week Week number
     */
    suspend fun bookings(week: Int? = null): List<Booking> {
        val rawBookingResult = this.apiManager.getObj<RawBookingsResult>(
            getHostUrl(HOST_BOOKINGS) + (week?.toString() ?: "")
        )

        if (rawBookingResult.rsvWebDto.isEmpty())
            error("No booking was found for this week.")

        return rawBookingResult.rsvWebDto.map { rawBooking -> Booking.decodeFromRawBooking(rawBooking) }
    }

    /**
     * Book a meal on a given date.
     *
     * @param bookingId Booking id (usually there's one booking per week and per terminal)
     * @param day Day of week to book (1-7)
     * @param reservations Number of reservations to book
     * @param bookEvening Should we book for the evening ?
     */
    suspend fun bookMeal(bookingId: String, day: Short, reservations: Short = 1, bookEvening: Boolean = false): BookingDay {
        val rawBook = this.apiManager.postObj<RawBookResult>(getHostUrl(HOST_BOOK_MEAL), buildJsonObject {
            put("dayOfWeek", day)
            put("dayReserv", reservations)
            putJsonObject("web") {
                put("id", bookingId)
            }
            put("hasHoteResaSoirActive", bookEvening)
        }.toString())

        return BookingDay(
            rawBook.dayReserv > 0,
            true,
            rawBook.dayOfWeek,
            rawBook.msg ?: "",
            rawBook.dayReserv,
            Clock.System.now()
        )
    }
}
