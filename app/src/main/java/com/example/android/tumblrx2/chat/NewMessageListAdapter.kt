package com.example.android.tumblrx2.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.responses.chatsresponse.ChatObject
import com.example.android.tumblrx2.responses.chatsresponse.Data
import androidx.core.content.ContextCompat.startActivity

import android.R.id
import androidx.core.content.ContextCompat
import com.example.android.tumblrx2.responses.following.FollowingBlog


class NewMessageListAdapter(context:Context,following:List<FollowingBlog>): BaseAdapter() {

    private val myContext: Context
    private val myFollowing:List<FollowingBlog>


    init {
        myContext=context
        myFollowing=following

    }
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
        val chat:FollowingBlog=getItem(position)
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.messages_list_item,parent,false)
        row.findViewById<TextView>(R.id.username_text).text=chat.handle
        row.findViewById<TextView>(R.id.snippet_msg).text=""
        row.setOnClickListener{
            val i = Intent(myContext, ActivityChat::class.java)
            i.putExtra("id", chat._id)
            i.putExtra("name", chat.handle)
            myContext.startActivity(i)
//            myContext.startActivity(Intent(myContext, ActivityChat::class.java))
        }
        return row
    }
}