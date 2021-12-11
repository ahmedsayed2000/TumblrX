package com.example.android.tumblrx2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class MessagesListAdapter(context:Context): BaseAdapter() {

    private val myContext: Context

    init {
        myContext=context
    }
    override fun getCount(): Int {
        return 8
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.messages_list_item,parent,false)
        row.findViewById<TextView>(R.id.username_text).text="someone_".plus(position)
        row.findViewById<TextView>(R.id.snippet_msg).text="Text Message ".plus(position)
        return row
    }
}