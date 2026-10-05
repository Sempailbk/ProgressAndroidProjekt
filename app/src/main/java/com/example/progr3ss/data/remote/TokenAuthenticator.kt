package com.example.progr3ss.data.remote

import com.example.progr3ss.data.local.TokenStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException

class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val refreshApi: ApiService,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) return null
        return synchronized(this) { refreshAndRetry(response) }
    }

    private fun refreshAndRetry(response: Response): Request? {
        val sentToken = response.request.header("Authorization")?.removePrefix("Bearer ")
        val currentToken = runBlocking { tokenStore.accessToken.first() }
        if (currentToken != null && currentToken != sentToken) {
            return response.request.withToken(currentToken)
        }

        val refreshToken = runBlocking { tokenStore.refreshToken.first() } ?: return null

        return try {
            val newTokens = runBlocking { refreshApi.refresh("Bearer $refreshToken") }
            runBlocking { tokenStore.save(newTokens) }
            response.request.withToken(newTokens.accessToken)
        } catch (e: HttpException) {
            runBlocking { tokenStore.clear() }
            null
        } catch (e: IOException) {
            null
        }
    }

    private fun Request.withToken(token: String): Request =
        newBuilder().header("Authorization", "Bearer $token").build()
}