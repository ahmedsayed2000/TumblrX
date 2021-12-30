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
import kotlinx.coroutines.launch

class PostsFragment:Fragment() {
    private lateinit var viewModel: PostsViewModel
    private lateinit var adapter: PostAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPref = this.requireActivity().getSharedPreferences("appPref", Context.MODE_PRIVATE)

        var view:View=inflater.inflate(R.layout.fragment_posts, container, false);
        viewModel = ViewModelProvider(this)[PostsViewModel::class.java]
        var postsList= view.findViewById<ListView>(R.id.posts_list)
        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            val blogId = sharedPref.getString("primaryBlogId", null)
            val response = viewModel.getBlogPosts(token!!,blogId!!)
                if (response.isSuccessful) {
                           postsList.adapter= PostsListsAdapter(requireContext(),response.body()!!.data)
                } else {
                }
        }
        return view
    }
}