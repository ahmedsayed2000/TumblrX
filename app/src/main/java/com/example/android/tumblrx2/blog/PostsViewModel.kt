package com.example.android.tumblrx2.blog

import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.postsdata.PostsResponse
import retrofit2.Response

class PostsViewModel : ViewModel() {

    lateinit var posts : PostsResponse

    suspend fun getBlogPosts(token:String,id:String) : Response<PostsResponse>{
        return RetrofitInstance.api.getBlogPosts(token,id)
    }
}