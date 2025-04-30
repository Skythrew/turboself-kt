package core

import dto.AuthOptions
import dto.AuthResponse
import dto.RawBalance
import dto.RawHome
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import models.AuthInfos
import models.Balance
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
     * Get the latest payment.
     */
    suspend fun latestPayment(): Payment? {
        val rawHome = this.apiManager.getObj<RawHome>(getHostUrl(HOST_HOME))

        if (rawHome.latestPaiement == null)
            return null

        return Payment.decodeFromRawPayment(this.authInfos!!.hostId, rawHome.latestPaiement)
    }
}
