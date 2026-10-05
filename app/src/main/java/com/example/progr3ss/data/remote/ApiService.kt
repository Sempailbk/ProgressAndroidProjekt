package com.example.progr3ss.data.remote

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService{
    @POST("auth/local/sighnin")
    suspend fun signIn(@Body body: SignInRequest): AuthResponse

    @Multipart
    @POST("auth/local/signup")
    suspend fun signUp(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ):AuthResponse

    @POST("auth/local/refresh")
    suspend fun refresh(@Header("Authorization") bearer:String): Tokens
    @GET("profile")
    suspend fun getMyProfile(): ProfileDto
}