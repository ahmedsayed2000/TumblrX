package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.addpost.addpostfragments.BlogEntity
import com.example.android.tumblrx2.addpost.addpostfragments.BlogSearch
import com.example.android.tumblrx2.responses.LoginResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

 interface AddPostApi {




    @Multipart
    @POST("/api/blog/{blogid}/posts")
    fun postToBlog(
        @Path("blogid") id: String,
        @Part state: MultipartBody.Part,
        @Part content: MutableList<MultipartBody.Part>,
        @Part tag: MutableList<MultipartBody.Part>,
        @Header("Authorization") header: String,
        @Part fileList: MutableList<MultipartBody.Part>?
    ): Call<LoginResponse>


    @GET("/api/blog/search")
    fun getTagSearch(@Query("q") blogSearch: String): Call<BlogSearchList>


     @GET("api/user/get-blogs")
     fun getBlogs(@Header("Authorization") header: String): Call<MutableList<BlogEntity>>

 }