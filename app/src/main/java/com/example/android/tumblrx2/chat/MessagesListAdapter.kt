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


class MessagesListAdapter(context:Context,chats:List<Data>): BaseAdapter() {

    private val myContext: Context
    private val myChats:List<Data>


    /**
     * Sets context and chats list for adapter
     */
    init {
        myContext=context
        myChats=chats

    }
    override fun getCount(): Int {
        return myChats.count()
    }

    override fun getItem(position: Int): Data {
        return myChats[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val chat:Data=getItem(position)
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.messages_list_item,parent,false)
        row.findViewById<TextView>(R.id.username_text).text=chat.blogHandle
        row.findViewById<TextView>(R.id.snippet_msg).text=chat.message
        row.setOnClickListener{
            val i = Intent(myContext, ActivityChat::class.java)
            i.putExtra("id", chat.chatId)
            i.putExtra("name", chat.blogHandle)
            myContext.startActivity(i)
//            myContext.startActivity(Intent(myContext, ActivityChat::class.java))
        }
        return row
    }
}