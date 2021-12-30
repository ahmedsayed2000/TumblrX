package com.example.android.tumblrx2.chat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.blog.FollowersListAdapter
import com.example.android.tumblrx2.login.BlogModelView
import kotlinx.coroutines.launch

class NewMessageActivity : AppCompatActivity() {
    private lateinit var viewModel: BlogModelView
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        setTheme(R.style.Theme_TumblrX2)
        var followersList= findViewById<ListView>(R.id.followers_list)
        viewModel = ViewModelProvider(this)[BlogModelView::class.java]
        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            val response = viewModel.getFollowing(token!!)
            if (response.isSuccessful) {
                followersList.adapter= NewMessageListAdapter(this@NewMessageActivity,response.body()!!.followingBlogs)
            } else {
            }
        }
//        followersListAdapter.adapter= MessagesListAdapter(this)
    }
}