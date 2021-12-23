package com.example.android.tumblrx2.data.repository

import com.example.android.tumblrx2.data.UserPreferences
import com.example.android.tumblrx2.network.LoginSignupAPI

class LoginSignupRepository(
    private val api: LoginSignupAPI,
    private val preferences: UserPreferences
) : BaseRepository() {
    /**
     * Uses a LoginSignupAPI instance to hit the backend using [email] and [password]
     */
    suspend fun login(email: String, password: String) = safeApiCall { api.login(email, password) }

    /**
     * saves the authentication token given when a user logs in in the dataStore     using [token]
     */
    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }
}