package com.example.progr3ss.data.remote

import com.example.progr3ss.data.local.TokenStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // If a request already sets its own Authorization header, leave it alone
        // (the refresh call will do this in part 2).
        if (original.header("Authorization") != null) return chain.proceed(original)

        val token = runBlocking { tokenStore.accessToken.first() }
        val request = if (token != null) {
            original.newBuilder().header("Authorization", "Bearer $token").build()
        } else original

        return chain.proceed(request)
    }
}