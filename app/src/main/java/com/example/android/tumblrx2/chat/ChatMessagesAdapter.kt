package com.example.android.tumblrx2.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.android.tumblrx2.R


class ChatMessagesAdapter(context:Context): BaseAdapter() {

    private val myContext: Context

    init {myContext=context}

    override fun getCount(): Int {
        return 10
    }
    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.activity_chat_message,parent,false)
        return row
    }
}