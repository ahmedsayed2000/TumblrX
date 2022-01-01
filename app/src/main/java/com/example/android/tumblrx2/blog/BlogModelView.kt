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

    /**
     * Validates that [title] is not and empty string
     * Return -1 if [title] is empty and 0 if not
     */
    fun validateInput(title: String): Int {
        return if (title.isEmpty()) -1
        else 0
    }

    /**
     * Returns error message based on [errCode]
     */
    fun chooseErrMsg(errCode: Int): String {
        return when (errCode) {
            -1 -> "Blog Name Is Required"
            else -> ""
        }
    }

    /**
     * Triggers API call for creating a new blog
     */
    suspend fun createBlog(title: String,id:String,token: String): Response<CreateBlogResponse> {
        return RetrofitInstance.api.createBlog(token,title,title,false,id)
    }

    /**
     * Triggers API call for getting a user's info
     */
    suspend fun getInfo(token: String): Response<InfoResponse> {
        return RetrofitInstance.api.getInfo(token)
    }

    /**
     * Triggers API call for getting the list of blogs a user follows
     */
    suspend fun getFollowing(token: String): Response<FollowingResponse> {
        return RetrofitInstance.api.getFollowing(token)
    }

    /**
     * Triggers API call for getting a list of posts a user likes
     */
    suspend fun getLikes(token: String): Response<LikesResponse> {
        return RetrofitInstance.api.getLikes(token)
    }
}