package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.data.responses.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginSignupAPI {

    @FormUrlEncoded
    @POST("/api/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}