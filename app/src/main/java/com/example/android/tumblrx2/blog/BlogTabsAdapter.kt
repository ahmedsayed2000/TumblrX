package com.example.android.tumblrx2.blog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.tumblrx2.activity.ActivityFragment

class BlogTabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val titles = arrayOf("Posts","Likes","Following")
    override fun getItemCount(): Int {
        return titles.size;
    }
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ActivityFragment()
            1 -> return ActivityFragment()
            2 -> return ActivityFragment()
        }
        return ActivityFragment()
    }
}