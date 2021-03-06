package com.example.android.tumblrx2.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.android.tumblrx2.R

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        setTheme(R.style.Theme_TumblrX2)
        var followersListAdapter= findViewById<ListView>(R.id.followers_list)
        followersListAdapter.adapter= MessagesListAdapter(this)
    }
}