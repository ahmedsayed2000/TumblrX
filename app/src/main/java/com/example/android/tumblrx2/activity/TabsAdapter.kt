package com.example.android.tumblrx2.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.tumblrx2.activity.ActivityFragment
import com.example.android.tumblrx2.chat.MessagesFragment

class TabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val titles = arrayOf("Activity","Messages")
    override fun getItemCount(): Int {
        return titles.size;
    }
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ActivityFragment()
            1 -> return MessagesFragment()
        }
        return MessagesFragment()
    }
}