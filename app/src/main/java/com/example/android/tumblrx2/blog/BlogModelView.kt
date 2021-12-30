package com.example.android.tumblrx2.login

import androidx.lifecycle.*
import com.example.android.tumblrx2.responses.LoginResponse
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.CreateBlogResponse
import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.following.FollowingResponse
import com.example.android.tumblrx2.responses.likes.LikesResponse
import retrofit2.Response

class BlogModelView() : ViewModel() {
    fun validateInput(title: String): Int {
        return if (title.isEmpty()) -1
        else 0
    }

    fun chooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Blog Name Is Required"
            else -> ""
        }
    }

    suspend fun createBlog(title: String,id:String,token: String): Response<CreateBlogResponse> {
        return RetrofitInstance.api.createBlog(token,title,title,false,id)
    }

    suspend fun getInfo(token: String): Response<InfoResponse> {
        return RetrofitInstance.api.getInfo(token)
    }

    suspend fun getFollowing(token: String): Response<FollowingResponse> {
        return RetrofitInstance.api.getFollowing(token)
    }

    suspend fun getLikes(token: String): Response<LikesResponse> {
        return RetrofitInstance.api.getLikes(token)
    }
}