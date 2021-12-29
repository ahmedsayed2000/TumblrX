package com.example.android.tumblrx2.activity

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.android.tumblrx2.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class ActivityFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View=inflater.inflate(R.layout.fragment_activity, container, false);
        var bottomBtn=view.findViewById<Button>(R.id.bottomSheetBtn)
        bottomBtn.setOnClickListener {showBottomSheet()}
        var activityList= view.findViewById<ListView>(R.id.activity_list)
        activityList.adapter= ActivityListAdapter(requireContext())
        return view
    }

    private fun showBottomSheet()
    {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        bottomSheetDialog.show()
    }

}