package com.example.android.tumblrx2.home

import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.dashboardpost.DashboardPost
import retrofit2.Response

class HomeViewModel : ViewModel() {

    lateinit var userInfo : InfoResponse

    /**
     * uses the Retrofit instance function getInfo to hit the backend
     * returns a response object which is parsed in the activity and extracts the user info
     * known through the given [token]
     */
    suspend fun getUserInfo(token:String) : Response<InfoResponse>{
        return RetrofitInstance.api.getInfo(token)
    }


    /**
     * uses the Retrofit instance function getDashboardPosts to hit the backend
     * return a response object parsed by the activity, which contains a list of posts related to the current user
     * known through the given [token]
     */
    suspend fun getDashboardPosts(token: String): Response<DashboardPost> {
        return RetrofitInstance.api.getDashboardPosts(token)

    }

}