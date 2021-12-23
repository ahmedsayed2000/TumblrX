package com.example.android.tumblrx2.data.repository

import com.example.android.tumblrx2.data.UserPreferences
import com.example.android.tumblrx2.network.LoginSignupAPI
import com.example.android.tumblrx2.network.UserApi

class UserRepository(
    private val api: UserApi,
) : BaseRepository() {

    /**
     * Gets info of a logged in user, using UserApi
     */
    suspend fun getUser() = safeApiCall {
        api.getUser()
    }
}

