package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.responses.CreateBlogResponse
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.responses.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

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

    @FormUrlEncoded
    @POST("/api/blog/61b2131d0b8aaec60c5af0eb")
    suspend fun createBlog(
        @Header("Authorization") header: String,
        @Field("title") title: String,
        @Field("handle") handle: String,
        @Field("private") private: Boolean,
    ): Response<CreateBlogResponse>


}