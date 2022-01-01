package com.example.android.tumblrx2

import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager2.widget.ViewPager2
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.blog.ActivityBlog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityBlogTest {

    @get:Rule
    val mActivityScenarioRule = ActivityScenarioRule(ActivityBlog::class.java)

    @Before
    fun setUp() {
    }


    @Test
    fun testUserName() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.blog_name) as TextView
            assertNotNull(view)
        }
    }

    @Test
    fun testTabs() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.tabLayout2) as TabLayout
            assertNotNull(view)
        }
    }

    @Test
    fun testPages() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.viewPager2) as ViewPager2
            assertNotNull(view)
        }
    }

    @Test
    fun testNabar() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.bottom_navbar) as BottomNavigationView
            assertNotNull(view)
        }
    }

    @After
    fun tearDown() {
    }
}