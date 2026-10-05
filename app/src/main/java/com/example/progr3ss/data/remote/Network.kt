package com.example.progr3ss.data.remote

import com.example.progr3ss.data.local.TokenStore
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class Network(tokenStore: TokenStore) {

    private val json = Json { ignoreUnknownKeys = true }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        redactHeader("Authorization")
    }

    private fun buildApi(client: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)

    private val refreshApi: ApiService = buildApi(
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    )

    val api: ApiService = buildApi(
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .authenticator(TokenAuthenticator(tokenStore,refreshApi))
            .addInterceptor(logging)
            .build()
    )

    companion object {
        fun String.toTextPart(): RequestBody = toRequestBody("text/plain".toMediaType())
        const val BASE_URL = "http://localhost:8081/"
    }
}