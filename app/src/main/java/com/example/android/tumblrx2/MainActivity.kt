package com.example.android.tumblrx2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.android.tumblrx2.data.UserPreferences
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
            val userPreferences = UserPreferences(this)
            userPreferences.authToken.asLiveData().observe(this, Observer { token ->
                if (token != null) {
                    startActivity(Intent(this@MainActivity, HomePageActivity::class.java).also{
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else
                    startActivity(Intent(this, IntroActivity::class.java))
            })
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