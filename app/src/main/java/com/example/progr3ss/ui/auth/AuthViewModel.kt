package com.example.progr3ss.ui.auth

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.progr3ss.Progr3ssApp
import com.example.progr3ss.data.remote.Network.Companion.toTextPart
import com.example.progr3ss.data.remote.SignInRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Array.set

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val app: Progr3ssApp get() = getApplication()

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        error = null
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (loading) return
        viewModelScope.launch {
            loading = true
            error = null
            try {
                val auth = app.network.api.signIn(SignInRequest(email.trim(), password))
                app.tokenStore.save(auth.tokens)
                onSuccess()
            } catch (e: HttpException) {
                error = when (e.code()) {
                    401, 403 -> "Incorrect email or password."
                    else -> "Something went wrong (code ${e.code()})."
                }
            } catch (e: IOException) {
                error = "Could not reach the server. Check your connection."
            } finally {
                loading = false
            }
        }
    }

    var passwordMismatch by mutableStateOf(false)
        private set
    fun register(username:String,email:String,password:String, confirmPassword:String,onSuccess:()->Unit){
        if(loading) return
        passwordMismatch = password != confirmPassword
        if(passwordMismatch){
            error = "Passwords do not match."
            return
        }
        viewModelScope.launch {
            loading = true
            error = null
            try{
                val auth = app.network.api.signUp(
                    username.trim().toTextPart(),
                    email.trim().toTextPart(),
                    password.toTextPart(),
                )
                app.tokenStore.save(auth.tokens)
                onSuccess()
            } catch(e: HttpException){
                error= when(e.code()){
                    400 -> "Please check your details and try again."
                    409 -> "An account with this email already exists."
                    else -> "Something went wrong (code ${e.code()})."
                }
            } catch(e: IOException){
                error = "Could not reach the server. Check your connection."
            } finally{
                loading = false
            }
        }
    }
}