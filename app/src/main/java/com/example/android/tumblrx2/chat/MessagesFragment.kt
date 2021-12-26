package com.example.android.tumblrx2.chat

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import com.example.android.tumblrx2.R


class MessagesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View= inflater.inflate(R.layout.fragment_messages, container, false);
        var messagesList= view.findViewById<ListView>(R.id.messages_list)
        messagesList.adapter= MessagesListAdapter(requireContext())
        return view
    }
}