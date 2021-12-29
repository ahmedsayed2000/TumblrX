package com.example.android.tumblrx2.blog

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.android.tumblrx2.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class FollowersFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View=inflater.inflate(R.layout.fragment_followers, container, false);
        var activityList= view.findViewById<ListView>(R.id.followers_list)
        activityList.adapter= FollowersListAdapter(requireContext())
        return view
    }

//    private fun showBottomSheet()
//    {
//        val bottomSheetDialog = BottomSheetDialog(requireContext())
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
//        bottomSheetDialog.show()
//    }

}