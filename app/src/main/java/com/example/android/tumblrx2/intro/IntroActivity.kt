package com.example.android.tumblrx2.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.android.tumblrx2.HomePageActivity
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.data.UserPreferences

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_intro)
        val introOptionsFragment = IntroOptionsFragment()

        val userPreferences = UserPreferences(this)
        userPreferences.authToken.asLiveData().observe(this, Observer{
            if (it != null){
                startActivity(Intent(this@IntroActivity, HomePageActivity::class.java))
            }
        })

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout_fragment, introOptionsFragment)
            commit()
        }

    }
}