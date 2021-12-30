package com.example.android.tumblrx2.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.responses.postsdata.Data
import com.example.android.tumblrx2.responses.postsdata.PostsResponse


class PostsListsAdapter(context:Context,posts:List<Data>): BaseAdapter() {

    private val myContext: Context
    private val myPosts: List<Data>


    init {
        myContext=context
        myPosts=posts
    }

    override fun getCount(): Int {
        return myPosts.count()
    }
    override fun getItem(position: Int): Data {
        return myPosts[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val post:Data = getItem(position)
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.post_item_blog,parent,false)
        row.findViewById<TextView>(R.id.blog_name).text=post.blogAttribution.handle
        row.findViewById<TextView>(R.id.post_text).text=post.content[0].text
        return row
    }
}