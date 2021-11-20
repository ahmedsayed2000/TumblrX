package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val introOptionsFragment = IntroOptionsFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout_fragment, introOptionsFragment)
            commit()
        }
    }
}