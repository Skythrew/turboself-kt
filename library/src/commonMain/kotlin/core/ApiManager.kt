package core

import dto.ErrorResponse
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ApiManager {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private val headers = mutableMapOf<String, String>()

    @OptIn(InternalAPI::class)
    private suspend fun request(
        url: String,
        httpMethod: HttpMethod,
        httpHeaders: Map<String, String> = mapOf(),
        httpBody: Any? = null
    ): HttpResponse {
        val response = httpClient.request(url) {
            method = httpMethod
            contentType(ContentType.Application.Json)
            userAgent("@skythrew/turboselfkt")
            headers {
                httpHeaders.map { (key, value) -> header(key, value) }
            }
            setBody(httpBody)
        }

        if (response.status.value >= 400 ) {
            val errorObj = Json.decodeFromString<ErrorResponse>(response.bodyAsText())
            error("Turboself Error (${response.status.value}): ${errorObj.message}")
        }

        return response
    }

    suspend fun get(url: String): HttpResponse {
        return this.request(url, HttpMethod.Get)
    }

    suspend fun post(url: String, body: Any): HttpResponse {
        return this.request(url, HttpMethod.Post, httpBody = body)
    }

    fun setAuthToken(token: String) {
        this.headers["Authorization"] = "Bearer $token"
    }
}

fun main() {
    runBlocking {
        println(ApiManager().get("https://google.fr").request.headers)
    }
}