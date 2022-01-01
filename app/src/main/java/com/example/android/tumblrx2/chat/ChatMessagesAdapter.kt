package com.example.android.tumblrx2.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.responses.messages.Message


class ChatMessagesAdapter(context:Context,messages:List<Message>,name:String): BaseAdapter() {

    private val myContext: Context
    private val myMessages: List<Message>
    private val otherUser:String

    /**
     * Sets context and messages list for adapter
     */
    init {
        myContext=context
    myMessages=messages
    otherUser=name}

    override fun getCount(): Int {
        return myMessages.count()
    }
    override fun getItem(position: Int): Message {
        return myMessages[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var message=getItem(position)
        val layoutInflater=LayoutInflater.from(myContext)
        val row= layoutInflater.inflate(R.layout.activity_chat_message,parent,false)
        row.findViewById<TextView>(R.id.blog_username).text=otherUser
        row.findViewById<TextView>(R.id.message).text=message.text
        return row
    }
}