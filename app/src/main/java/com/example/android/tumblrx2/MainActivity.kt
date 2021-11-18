package com.example.android.tumblrx2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)    //resets theme to remove splash screen
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.goToHome.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }
        binding.btnIntro.setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
        }
    }
}