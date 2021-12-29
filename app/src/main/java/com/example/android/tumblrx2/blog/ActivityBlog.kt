package com.example.android.tumblrx2.blog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.android.tumblrx2.blog.ActivityCreateBlog
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.home.HomePageActivity
import com.example.android.tumblrx2.intro.IntroActivity
import com.giphy.sdk.analytics.GiphyPingbacks.sharedPref
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityBlog : AppCompatActivity() {
    var tabLayout: TabLayout? = null;
    var viewPager: ViewPager2? = null;
    var myAdapter: BlogTabsAdapter?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TumblrX2)
        setContentView(R.layout.activity_blog)
        val sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE)
        tabLayout = findViewById(R.id.tabLayout2)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Posts"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Likes"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Following"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        viewPager = findViewById(R.id.viewPager2)
        myAdapter = BlogTabsAdapter(supportFragmentManager,lifecycle)
        viewPager!!.adapter=myAdapter
        TabLayoutMediator(tabLayout!!,viewPager!!){tab,position->
            when (position) {
                0 -> tab.text="Posts"
                1 -> tab.text="Likes"
                2 -> tab.text="Following"
            }
            viewPager!!.currentItem = tab.position
        }.attach()
        var bottomBtn=findViewById<TextView>(R.id.blog_name)
        bottomBtn.setOnClickListener {showBottomSheet()}

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavBar.selectedItemId = R.id.ic_profile

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
                else -> {
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
            }
        }

    }

    private fun showBottomSheet()
    {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.blog_bottom_sheet_layout);
        bottomSheetDialog.show()
        val createBtn=bottomSheetDialog.findViewById<LinearLayout>(R.id.create_blog)
        createBtn!!.setOnClickListener {
            startActivity(Intent(this, ActivityCreateBlog::class.java))
        }
    }

}