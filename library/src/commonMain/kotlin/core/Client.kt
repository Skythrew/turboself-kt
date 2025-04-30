package core

import dto.AuthOptions
import dto.AuthResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import models.AuthInfos

/**
 * Turboself Client
 */
class Client {
    private val apiManager = ApiManager()

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

        return AuthInfos.decodeFromAuthResponse(authResponse)
    }
}
