package com.example.progr3ss

import android.app.Application
import com.example.progr3ss.data.local.TokenStore
import com.example.progr3ss.data.remote.Network

class Progr3ssApp : Application() {
    val tokenStore by lazy { TokenStore(this) }
    val network by lazy { Network(tokenStore) }
}