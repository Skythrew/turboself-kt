package core

import dto.ErrorResponse
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

class ApiManager {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val jsonDecoder = Json { ignoreUnknownKeys = true }

    private var bearer: String? = null

    @OptIn(InternalAPI::class)
    private suspend fun request(
        url: String,
        httpMethod: HttpMethod,
        httpHeaders: Map<String, String> = mapOf(),
        httpBody: Any? = null
    ): HttpResponse {
        val response = httpClient.request(url) {
            contentType(ContentType.Application.Json)

            method = httpMethod
            bearer?.let { bearerAuth(it) }

            headers {
                httpHeaders.map { (key, value) -> header(key, value) }
            }

            if(httpBody != null) setBody(httpBody)
        }

        if (response.status.value >= 400 ) {
            val errorObj = jsonDecoder.decodeFromString<ErrorResponse>(response.bodyAsText())
            error("Turboself Error (${response.status.value}): ${errorObj.message}")
        }

        return response
    }

    suspend fun get(url: String): HttpResponse {
        return this.request(url, HttpMethod.Get)
    }

    suspend inline fun <reified T> getObj(url: String): T {
        return this.jsonDecoder.decodeFromString<T>(this.get(url).bodyAsText())
    }

    suspend fun post(url: String, body: Any): HttpResponse {
        return this.request(url, HttpMethod.Post, httpBody = body)
    }

    fun setAuthToken(token: String) {
        this.bearer = token
    }
}
