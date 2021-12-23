package com.example.android.tumblrx2.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.tumblrx2.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)


        setContentView(R.layout.activity_intro)
        val introOptionsFragment = IntroOptionsFragment()


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout_fragment, introOptionsFragment)
            commit()
        }

    }
}