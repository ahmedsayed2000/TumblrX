package com.example.android.tumblrx2.chat

import androidx.lifecycle.*
import com.example.android.tumblrx2.network.RetrofitInstance
import com.example.android.tumblrx2.responses.chatsresponse.ChatsResponse
import com.example.android.tumblrx2.responses.messages.MessagesResponse
import retrofit2.Response

class MessagesModelView() : ViewModel() {

    /**
     * Triggers API call to fetch messages in a chat
     */
    suspend fun getChatMessages(id:String,token:String) :Response<MessagesResponse>{
        return RetrofitInstance.api.getChatsMessages(token,id)
    }

}
