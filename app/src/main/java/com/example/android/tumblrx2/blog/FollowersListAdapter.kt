package com.example.android.tumblrx2.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.tumblrx2.R


class FollowersListAdapter(context:Context): BaseAdapter() {

    private val myContext: Context

    init {myContext=context}

    override fun getCount(): Int {
        return 2
    }
    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.followers_list_item,parent,false)
//        row.findViewById<TextView>(R.id.row_text).text="someone_".plus(position).plus(" started following mariam.a.malak")
        return row
    }
}