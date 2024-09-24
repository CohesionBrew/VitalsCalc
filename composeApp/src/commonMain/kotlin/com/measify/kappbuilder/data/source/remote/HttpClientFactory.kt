package com.measify.kappbuilder.data.source.remote

import com.measify.kappbuilder.util.logging.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object HttpClientFactory {
    fun default() = HttpClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "BASE_URL"
            }
            header(HttpHeaders.ContentType, "application/json")
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    AppLogger.d("NetworkRequest: $message")
                }
            }
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }.also {
        it.plugin(HttpSend).intercept { request ->
            //For all requests you can send user token here, for example
            val userToken = ""
            request.header("Authorization", "Bearer $userToken")
            execute(request)
        }
    }


}

