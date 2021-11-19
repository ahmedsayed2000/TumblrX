package com.example.android.tumblrx2

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomePageActivityTest {

    @get:Rule
    val mHomePageRule = ActivityScenarioRule(HomePageActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun testLaunch() {
        mHomePageRule.scenario.onActivity {
            val view = it.findViewById(R.id.button_addPost) as FloatingActionButton
            assertNotNull(view)
        }
    }

    @After
    fun tearDown() {
    }
}