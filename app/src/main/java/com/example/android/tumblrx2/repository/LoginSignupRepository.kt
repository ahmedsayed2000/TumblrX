package com.example.android.tumblrx2.repository

import com.example.android.tumblrx2.network.LoginSignupAPI

class LoginSignupRepository(private val api: LoginSignupAPI) : BaseRepository() {
    suspend fun login(email: String, password: String) = safeApiCall { api.login(email, password) }
}