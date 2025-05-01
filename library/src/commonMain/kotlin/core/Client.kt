package core

import dto.AuthOptions
import dto.AuthResponse
import dto.RawBalance
import dto.RawEstablishment
import dto.RawHistoryEvent
import dto.RawHome
import dto.RawPayment
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import models.AuthInfos
import models.Balance
import models.Establishment
import models.HistoryEvent
import models.Payment

/**
 * Turboself Client
 */
class Client {
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

        val authInfos = AuthInfos.decodeFromAuthResponse(authResponse)

        this.authInfos = authInfos

        return authInfos
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
}
