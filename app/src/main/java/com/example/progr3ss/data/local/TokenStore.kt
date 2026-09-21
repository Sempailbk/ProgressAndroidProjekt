package com.example.progr3ss.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.progr3ss.data.remote.Tokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth")

class TokenStore(context: Context) {
    private val store = context.applicationContext.dataStore

    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")

    val accessToken: Flow<String?> = store.data.map { it[accessKey] }
    val refreshToken: Flow<String?> = store.data.map { it[refreshKey] }

    suspend fun save(tokens: Tokens) {
        store.edit {
            it[accessKey] = tokens.accessToken
            it[refreshKey] = tokens.refreshToken
        }
    }

    suspend fun clear() {
        store.edit { it.clear() }
    }
}