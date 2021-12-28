package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.responses.RegisterResponse
import com.example.android.tumblrx2.responses.dashboarddata.DashboardResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/register")
    suspend fun signup(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("api/user/info")
    suspend fun getInfo(
        @Header("Authorization") token: String
    ): Response<InfoResponse>

    @GET("api/user/dashboard")
    suspend fun getDashboardPosts(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10, @Query("page") page: Int = 1
    ): Response<DashboardResponse>
}