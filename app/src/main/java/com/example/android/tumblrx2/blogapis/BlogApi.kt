package com.example.android.tumblrx2.blogapis

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface BlogApi {

    @POST("/blog")
    fun createBlog(@Query("id") id: String,@Body body: CreateBlogBody): Response<CreateBlogBody>
}