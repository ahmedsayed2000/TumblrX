package com.example.android.tumblrx2.network

import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.addpost.addpostfragments.BlogEntity
import com.example.android.tumblrx2.addpost.addpostfragments.BlogSearch
import com.example.android.tumblrx2.responses.LoginResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * the api used in add post request
 */
 interface AddPostApi {


    /**
     * creating a post with the specified data
     * @param[id] the blog id
     * @param[state] the post state
     * @param[content] the post content
     * @param[tag] post tags
     * @param[fileList] the list of files like images and video
     */
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


    /**
     * get the blogs handles with the specified
     * @param[blogSearch] the blog handle that matches the specified string
     */
    @GET("/api/blog/search")
    fun getTagSearch(@Query("q") blogSearch: String): Call<BlogSearchList>


    /**
     * getting the user blogs info
     * @param[header] the token of the user
     */
     @GET("api/user/get-blogs")
     fun getBlogs(@Header("Authorization") header: String): Call<MutableList<BlogEntity>>

 }