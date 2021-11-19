package com.example.android.tumblrx2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityAndMessagesActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null;
    var viewPager: ViewPager2? = null;
    var myAdapter: TabsAdapter?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_and_activity)
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
    }
}