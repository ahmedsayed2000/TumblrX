package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Activity responsible of adding a post
 */

class AddPostActivity : AppCompatActivity() {
    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
    }
}