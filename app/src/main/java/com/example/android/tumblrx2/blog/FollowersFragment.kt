package com.example.android.tumblrx2.blog

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.tumblrx2.PostAdapter
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.login.BlogModelView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch


class FollowersFragment: Fragment() {

    private lateinit var viewModel: BlogModelView
    private lateinit var adapter: FollowersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPref = this.requireActivity().getSharedPreferences("appPref", Context.MODE_PRIVATE)
        var view:View=inflater.inflate(R.layout.fragment_followers, container, false);
        var activityList= view.findViewById<ListView>(R.id.followers_list)
        viewModel = ViewModelProvider(this)[BlogModelView::class.java]
        lifecycleScope.launch {
            val token = sharedPref.getString("token", null)
            val response = viewModel.getFollowing(token!!)
            if (response.isSuccessful) {
                activityList.adapter= FollowersListAdapter(requireContext(),response.body()!!.followingBlogs,response.body()!!.numberOfFollowing)
                requireActivity().findViewById<TextView>(R.id.followers_text).text=response.body()!!.numberOfFollowing.toString().plus(" Tumblrs")
            } else {
            }
        }
        return view
    }

//    private fun showBottomSheet()
//    {
//        val bottomSheetDialog = BottomSheetDialog(requireContext())
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
//        bottomSheetDialog.show()
//    }

}