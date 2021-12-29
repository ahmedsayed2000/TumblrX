package com.example.android.tumblrx2.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.blog.ActivityBlog
import com.example.android.tumblrx2.home.HomePageActivity
import com.example.android.tumblrx2.intro.IntroActivity
import com.giphy.sdk.analytics.GiphyPingbacks
import com.giphy.sdk.analytics.GiphyPingbacks.sharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityAndMessagesActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null;
    var viewPager: ViewPager2? = null;
    var myAdapter: TabsAdapter?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_messages_and_activity)
        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Activity"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Messages"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        viewPager = findViewById(R.id.viewPager)
        myAdapter = TabsAdapter(supportFragmentManager,lifecycle)
        viewPager!!.adapter=myAdapter
        TabLayoutMediator(tabLayout!!,viewPager!!){tab,position->
            when (position) {
                0 -> tab.text="Activity"
                1 -> tab.text="Messages"
            }
            viewPager!!.currentItem = tab.position
        }.attach()


        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavBar.selectedItemId = R.id.ic_messages

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    startActivity(Intent(this, HomePageActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                R.id.ic_explore -> {
                    return@setOnItemSelectedListener false
//                    startActivity(Intent(this@HomePageActivity,HomePageActivity::class.java))
//                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
                R.id.ic_messages -> {
                    startActivity(
                        Intent(
                            this,
                            ActivityAndMessagesActivity::class.java
                        )
                    )
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                R.id.ic_profile -> {
                    startActivity(Intent(this, ActivityBlog::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    return@setOnItemSelectedListener false
                }
                R.id.ic_logout -> {
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(
                        Intent(
                            this, IntroActivity::class.java
                        )
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    return@setOnItemSelectedListener false
                }
                else ->{ return@setOnItemSelectedListener false }
            }
        }
    }
}