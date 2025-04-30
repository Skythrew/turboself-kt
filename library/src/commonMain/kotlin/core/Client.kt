package core

import dto.AuthOptions
import dto.AuthResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import models.AuthInfos

class Client {
    private val apiManager = ApiManager()

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
