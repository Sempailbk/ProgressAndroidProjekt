package com.example.progr3ss.data.remote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.security.AuthProvider
@Serializable
data class SignInRequest(val email: String, val password: String )

@Serializable
data class Tokens(val accessToken:String, val refreshToken: String)

@Serializable
data class ProfileDto(
    val id: Int,
    val email:String,
    val username:String? = null,
    val description: String? = null,
    val profileImageUrl:String? = null,
)

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    @SerialName("auth_provider") val authProvider: String,
    val profile: ProfileDto? = null,
    )
@Serializable
data class AuthResponse(val message:String, val user: UserDto,val tokens:Tokens)