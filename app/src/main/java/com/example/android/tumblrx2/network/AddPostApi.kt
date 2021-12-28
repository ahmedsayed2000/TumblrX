package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.addpost.addpostfragments.BlogSearch
import com.example.android.tumblrx2.responses.LoginResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

 interface AddPostApi {




    @Multipart
    @POST("/api/blog/61c1e3b86b827a7e144458f9/posts")
    fun postToBlog(
        @Part content: MutableList<MultipartBody.Part>,
        @Part tag: MutableList<MultipartBody.Part>,
        @Header("Authorization") header: String,
        @Part fileList: MutableList<MultipartBody.Part>?
    ): Call<LoginResponse>


    @GET("/api/blog/search")
    fun getBlogSearch(@Query("q") blogSearch: String): Call<BlogSearchList>


     @GET("/api/blog/search")
     suspend fun getBlogTags(@Query("q") blogSearch: String): Deferred<BlogSearchList>
}