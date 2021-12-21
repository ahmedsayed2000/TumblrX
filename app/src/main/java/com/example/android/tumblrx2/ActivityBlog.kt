package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
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
    }
}