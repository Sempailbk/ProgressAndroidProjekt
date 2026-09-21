package com.example.progr3ss.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService{
    @POST("auth/local/sighnin")
    suspend fun signIn(@Body body: SignInRequest): AuthResponse

    @POST("auth/local/refresh")
    suspend fun refresh(@Header("Authorization") bearer:String): Tokens
    @GET("profile")
    suspend fun getMyProfile(): ProfileDto
}