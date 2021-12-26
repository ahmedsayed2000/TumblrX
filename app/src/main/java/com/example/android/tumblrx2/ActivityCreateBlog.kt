package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityCreateBlog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_create_blog)

    }
}