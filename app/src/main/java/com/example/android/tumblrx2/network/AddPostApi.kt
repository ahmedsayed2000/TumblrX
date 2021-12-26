package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.responses.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface AddPostApi {


    @Multipart
    @POST("/api/blog/61c1e3b86b827a7e144458f9/posts")
    fun postToBlog(
        @Part content: MutableList<MultipartBody.Part>,
        @Header("Authorization") header: String,
        @Part fileList: MutableList<MultipartBody.Part>?
    ): Call<LoginResponse>
}