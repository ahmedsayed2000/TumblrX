package com.example.android.tumblrx2.chat

import androidx.lifecycle.*
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.chatsresponse.ChatsResponse
import retrofit2.Response

class ChatModelView() : ViewModel() {

    /**
     * Triggers API call to get a list of conversations
     */
     suspend fun getChats(token:String) :Response<ChatsResponse>{
        return RetrofitInstance.api.getChats(token)
    }


}