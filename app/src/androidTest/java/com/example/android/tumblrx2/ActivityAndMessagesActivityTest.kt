package com.example.android.tumblrx2

import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityAndMessagesActivityTest {

    @get:Rule
    val mActivityScenarioRule = ActivityScenarioRule(ActivityAndMessagesActivity::class.java)

    @Before
    fun setUp() {
    }


    @Test
    fun testUserName() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.nameText) as TextView
            assertNotNull(view)
        }
    }

    @Test
    fun testTabs() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.tabLayout) as TabLayout
            assertNotNull(view)
        }
    }

    @Test
    fun testPages() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.viewPager) as ViewPager2
            assertNotNull(view)
        }
    }

    @After
    fun tearDown() {
    }
}