package com.example.android.tumblrx2.home

import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.InfoResponse
import retrofit2.Response

class HomeViewModel : ViewModel() {

    lateinit var userInfo : InfoResponse

    suspend fun getUserInfo(token:String) : Response<InfoResponse>{
        return RetrofitInstance.api.getInfo(token)
    }
}