package com.example.android.tumblrx2.chat

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.tumblrx2.R

class ActivityChat  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_chat)
        var messagesList= findViewById<ListView>(R.id.chat_list)
        messagesList.adapter= ChatMessagesAdapter(this)
    }
}