package com.example.android.tumblrx2.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.InfoResponse
import com.example.android.tumblrx2.responses.dashboarddata.DashboardResponse
import retrofit2.Response

class HomeViewModel : ViewModel() {

    lateinit var userInfo : InfoResponse

    private val _postResponse = MutableLiveData<DashboardResponse>()
    val postResponse: LiveData<DashboardResponse>
            get() = _postResponse

    suspend fun getUserInfo(token:String) : Response<InfoResponse>{
        return RetrofitInstance.api.getInfo(token)
    }

    suspend fun getDashboardPosts(token: String): Response<DashboardResponse> {
        return RetrofitInstance.api.getDashboardPosts(token)
//        if(posts.isSuccessful){
//            _postResponse.value = posts.body()
//        }
    }



}