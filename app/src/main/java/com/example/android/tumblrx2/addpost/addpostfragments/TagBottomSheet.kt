package com.example.android.tumblrx2.addpost.addpostfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.tumblrx2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TagBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.tag_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topGroup = view.findViewById(R.id.top_chipGroup) as ChipGroup
        //var bottomGroup = view.findViewById(R.id.bottom_chip_group) as ChipGroup

        val chip1 = (topGroup.getChildAt(0)) as Chip

        chip1.setOnCloseIconClickListener {
            topGroup.removeViewAt(0)
        }


    }
}
