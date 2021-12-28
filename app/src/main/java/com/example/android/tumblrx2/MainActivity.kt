package com.example.android.tumblrx2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.databinding.ActivityMainBinding
import com.example.android.tumblrx2.home.HomePageActivity
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
        val sharedPref = this.getSharedPreferences("appPref",Context.MODE_PRIVATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.goToHome.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }
        binding.btnIntro.setOnClickListener {
            val token = sharedPref.getString("token", ".")
//            if (token != ".") {
//                Log.i("LoginActivity",token!!)
//                startActivity(Intent(this, HomePageActivity::class.java))
//            }
            //else {
                Log.i("LoginActivity","haha mafish token")
                startActivity(Intent(this, IntroActivity::class.java))
            //}
        }
        binding.btnActivity.setOnClickListener {
            startActivity(Intent(this, ActivityAndMessagesActivity::class.java))
        }
        binding.btnNewMessage.setOnClickListener {
            startActivity(Intent(this, NewMessageActivity::class.java))
        }
        binding.btnCreateBlog.setOnClickListener {
            startActivity(Intent(this, ActivityCreateBlog::class.java))
        }
        binding.btnBlog.setOnClickListener {
            startActivity(Intent(this, ActivityBlog::class.java))
        }
    }

}