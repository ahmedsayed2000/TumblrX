package com.example.android.tumblrx2.chat

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.responses.CreateBlogResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ActivityChat(): AppCompatActivity() {

    private lateinit var viewModel: MessagesModelView
    private lateinit var adapter: ChatMessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_chat)
        val b = intent.extras
        val id = b!!.getString("id")
        val name = b!!.getString("name")

        var messagesList= findViewById<ListView>(R.id.chat_list)
        viewModel = ViewModelProvider(this)[MessagesModelView::class.java]

        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            val response = viewModel.getChatMessages(id!!,token!!)
            if (response.isSuccessful) {
                messagesList.adapter= ChatMessagesAdapter(this@ActivityChat,response.body()!!.messages,name!!)
                this@ActivityChat.findViewById<TextView>(R.id.other_user).text=name
                this@ActivityChat.findViewById<TextView>(R.id.nameText).text=name
            } else {
            }
        }

    }
}