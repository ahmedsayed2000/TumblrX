package com.example.android.tumblrx2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.blog.ActivityBlog
import com.example.android.tumblrx2.blog.ActivityCreateBlog
import com.example.android.tumblrx2.chat.NewMessageActivity
import com.example.android.tumblrx2.databinding.ActivityMainBinding
import com.example.android.tumblrx2.home.HomePageActivity
import com.example.android.tumblrx2.intro.IntroActivity

/**
 * the starter activity of the app
 * @property binding the binding variable that contains the layout
 */

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        val sharedPref = this.getSharedPreferences("appPref", Context.MODE_PRIVATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val token = sharedPref.getString("token", null)
        if (token != null) {
            startActivity(Intent(this, HomePageActivity::class.java))
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
        }

    }

}