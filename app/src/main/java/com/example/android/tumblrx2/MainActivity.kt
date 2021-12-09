package com.example.android.tumblrx2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.databinding.ActivityMainBinding
import com.example.android.tumblrx2.intro.IntroActivity

/**
 * the starter activity of the app
 * @property binding the binding variable that contains the layout
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this activity
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.goToHome.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }
        binding.btnIntro.setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
        }
        binding.btnActivity.setOnClickListener {
            startActivity(Intent(this, ActivityAndMessagesActivity::class.java))
        }
        binding.btnNewMessage.setOnClickListener {
            startActivity(Intent(this, NewMessageActivity::class.java))
        }
    }

}