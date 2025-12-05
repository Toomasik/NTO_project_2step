package ru.myitschool.work.data.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.myitschool.work.core.Constants

object NetworkDataSource {
    private val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                        encodeDefaults = true
                    }
                )
            }
        }
    }

    suspend fun checkAuth(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.AUTH_URL))
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> error(response.bodyAsText())
            }
        }
    }

    suspend fun getInfo(code: String): Result<Boolean> = withContext(Dispatchers.IO) { // я так понял инфу аккаунта (image,name, id?)
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.AUTH_URL))
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> error(response.bodyAsText())
            }
        }
    }

    suspend fun getBooking(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        // типа передаем id как раз
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.AUTH_URL))
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> error(response.bodyAsText())
            }
        }
    }

    suspend fun createBook(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        // вот это логично выглядит ток не String а List какой нить т.к. мы передаем дату (дд.мм.гггг) и комнату
        return@withContext runCatching {
            val response = client.post(getUrl(code, "book"))
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> error(response.bodyAsText())
            }
        }
    }

    private fun getUrl(code: String, targetUrl: String) = "${Constants.HOST}/api/$code$targetUrl"
}