package com.example.android.tumblrx2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
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