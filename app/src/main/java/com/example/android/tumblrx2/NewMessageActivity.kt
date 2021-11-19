package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        var followersListAdapter= findViewById<ListView>(R.id.followers_list)
        followersListAdapter.adapter=MessagesListAdapter(this)
    }
}