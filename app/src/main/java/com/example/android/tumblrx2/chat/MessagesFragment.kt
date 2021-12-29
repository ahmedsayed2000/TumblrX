package com.example.android.tumblrx2.chat

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.blog.ActivityCreateBlog
import com.example.android.tumblrx2.login.BlogModelView
import kotlinx.coroutines.launch


class MessagesFragment: Fragment() {
    private lateinit var viewModel: ChatModelView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View= inflater.inflate(R.layout.fragment_messages, container, false);
        var messagesList= view.findViewById<ListView>(R.id.messages_list)
//        viewModel = ViewModelProvider(this)[ChatModelView::class.java]
//            val result =  viewModel.getChats().toString()
//            Toast.makeText(getApplicationContext(),
//                result,
//                Toast.LENGTH_SHORT).show();

        messagesList.adapter= MessagesListAdapter(requireContext())
        var newMessageBtn=view.findViewById<Button>(R.id.btn_new_message)
        newMessageBtn.setOnClickListener {
            val intent = Intent(activity, NewMessageActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    suspend fun apiCall(view: ChatModelView)
    {

    }


}