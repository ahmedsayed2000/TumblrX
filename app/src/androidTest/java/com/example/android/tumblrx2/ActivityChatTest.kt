package com.example.android.tumblrx2

import android.widget.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager2.widget.ViewPager2
import com.example.android.tumblrx2.activity.ActivityAndMessagesActivity
import com.example.android.tumblrx2.chat.ActivityChat
import com.google.android.material.tabs.TabLayout
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityChatTest {

    @get:Rule
    val mActivityScenarioRule = ActivityScenarioRule(ActivityChat::class.java)

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
    fun testChatList() {
        mActivityScenarioRule.scenario.onActivity {
            val button = it.findViewById(R.id.chat_list) as ListView
            assertNotNull(button)
            button.performClick()
        }
    }

    @Test
    fun testEditText() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.textMsg) as EditText
            assertNotNull(view)
        }
    }

    @Test
    fun testSendMsg() {
        mActivityScenarioRule.scenario.onActivity {
            val view = it.findViewById(R.id.sendMsg) as ImageView
            assertNotNull(view)
        }
    }

    @After
    fun tearDown() {
    }
}