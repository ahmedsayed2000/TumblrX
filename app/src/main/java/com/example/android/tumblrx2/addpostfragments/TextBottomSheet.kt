package com.example.android.tumblrx2.addpostfragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.tumblrx2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TextBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.text_bottomsheet, container, false)
    }


}