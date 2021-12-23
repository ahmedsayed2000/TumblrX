package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.data.responses.LoginResponse
import com.example.android.tumblrx2.login.User
import retrofit2.http.GET

interface UserApi {
    @GET("api/user/info")
    suspend fun getUser(): User
}