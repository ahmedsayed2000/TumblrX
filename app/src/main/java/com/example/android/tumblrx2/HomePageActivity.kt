package com.example.android.tumblrx2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.databinding.ActivityHomePageBinding

/**
 * Activity responsible of showing the home page of the user
 */

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)

        binding.buttonAddPost.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
    }
}