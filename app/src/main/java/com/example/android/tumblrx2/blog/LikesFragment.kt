package com.example.android.tumblrx2.blog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.PostAdapter
import com.example.android.tumblrx2.PostItem
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.home.HomeViewModel
import com.example.android.tumblrx2.login.BlogModelView
import kotlinx.coroutines.launch

class LikesFragment:Fragment() {
    private lateinit var viewModel: BlogModelView
    private lateinit var adapter: LikesListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPref = this.requireActivity().getSharedPreferences("appPref", Context.MODE_PRIVATE)

        var view:View=inflater.inflate(R.layout.fragment_likes, container, false);
        viewModel = ViewModelProvider(this)[BlogModelView::class.java]
        var likesList= view.findViewById<ListView>(R.id.likes_list)
        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            val response = viewModel.getLikes(token!!)
            if (response.isSuccessful) {
                likesList.adapter= LikesListAdapter(requireContext(),response.body()!!.likePosts)
            } else {
            }
        }
        return view
    }
}