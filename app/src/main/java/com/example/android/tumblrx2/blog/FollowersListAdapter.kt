package com.example.android.tumblrx2.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.responses.following.FollowingBlog


class FollowersListAdapter(context:Context,following:List<FollowingBlog>,number:Int): BaseAdapter() {

    private val myContext: Context
    private val myFollowing: List<FollowingBlog>
    private val followingNumber:Int

    /**
     * Constructor for Adapter, sets context and following list
     */
    init {myContext=context
    myFollowing=following
    followingNumber=number}

    override fun getCount(): Int {
        return myFollowing.count()
    }
    override fun getItem(position: Int): FollowingBlog {
        return myFollowing[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val following:FollowingBlog=getItem(position)
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.followers_list_item,parent,false)
        row.findViewById<TextView>(R.id.user_handle).text=following.handle
        row.findViewById<TextView>(R.id.blog_title).text=following.title
        return row
    }
}