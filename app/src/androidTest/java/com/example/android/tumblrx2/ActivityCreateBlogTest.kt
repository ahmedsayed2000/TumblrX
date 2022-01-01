package com.example.android.tumblrx2

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager2.widget.ViewPager2
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.blog.ActivityBlog
import com.example.android.tumblrx2.blog.ActivityCreateBlog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityCreateBlogTest {

    @get:Rule
    val mActivityScenarioRule = ActivityScenarioRule(ActivityCreateBlog::class.java)

    @Before
    fun setUp() {
    }


    @Test
    fun testButton() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.create_blog_btn) as Button
            assertNotNull(view)
        }
    }

    @Test
    fun testText() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.editTextTextPersonName3) as EditText
            assertNotNull(view)
        }
    }

    @After
    fun tearDown() {
    }
}