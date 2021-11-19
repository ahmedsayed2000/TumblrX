package com.example.android.tumblrx2.addpostfragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.databinding.FragmentInitialPostBinding
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * this is the starter fragment in the AddPostActivity
 */

class InitialPostFragment : Fragment() {


    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            val sheet = TextBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return super.onDown(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.root, InputMethodManager.SHOW_IMPLICIT)
            binding.firstText.requestFocus()
            return true
        }
    }

    private lateinit var gestureDetector: GestureDetectorCompat

    private lateinit var binding: FragmentInitialPostBinding


    private lateinit var mediaController: MediaController
    private var oneVideo: Boolean = false

    private val items = arrayOf("Image(s)", "Video/Gif")


    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val clipData = it.data?.clipData
                // Use the uri to load the image
                if (clipData != null) {
                    Log.d("Initial fragment", "clip data is not null")
                    addImages(clipData)
                } else {
                    val param = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    val image = ImageView(context)
                    image.setImageURI(it.data?.data)
                    param.setMargins(8, 8, 8, 10)
                    image.layoutParams = param
                    val index = getActiveText()
                    if (index == -1) {
                        Log.d("Initial fragment", "last view")
                        binding.postList.addView(image)
                    } else {
                        Log.d("Initial fragment", "view at index ${index + 1}")
                        binding.postList.addView(image, index + 1)
                    }
                    togglePostButton(true)
                }

            }
        }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                val video1 = VideoView(context)
                video1.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                //val video = binding.postVideo
                mediaController.setMediaPlayer(video1)
                mediaController.setAnchorView(video1)
                video1.setMediaController(mediaController)
                video1.setVideoURI(uri)
                video1.setZOrderOnTop(true)
                video1.visibility = View.VISIBLE
                binding.postList.addView(video1)
                togglePostButton(true)
                video1.start()
            }
        }

    /**
     * creates the Main layout and renders it
     * @param[savedInstanceState] a Bundle that has the extras sent to this fragment
     * @param[inflater] an inflater object to help inflate the fragment layout
     * @param[container] the root container of this fragment
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_initial_post, container, false)
        initialViews()
        mediaController = MediaController(context)
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        return binding.root
    }

    /**
     * this function initialise all fragment views and sets their event listeners
     */

    private fun initialViews() {


        binding.root.setOnClickListener {
            addText()
        }

        binding.scrollList.setOnClickListener {
            addText()
        }

        binding.postList.setOnClickListener {
            addText()
        }

        binding.firstText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() == "") {
                    if(!checkCount())
                        togglePostButton(false)
                    else
                        togglePostButton(true)
                } else
                    togglePostButton(true)

            }
        })

        /*binding.firstText.setOnTouchListener { view, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }*/



        binding.close.setOnClickListener {
            onDestroy()
        }

        binding.iconMore.setOnClickListener {
            val sheet = MoreBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }
        }

        binding.tags.setOnClickListener {
            val sheet = TagBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }
        }

        binding.tagsCard.setOnClickListener {
            val sheet = TagBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }
        }

        binding.insertText.setOnClickListener {
            val sheet = TextBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }
        }




        binding.insertMedia.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Media Type")
                    .setItems(items) { _, item ->
                        // Respond to item chosen
                        when (item) {
                            0 -> insertImage()
                            1 -> {
                                if (!oneVideo) {
                                    insertVideo()
                                    oneVideo = true
                                } else
                                    Toast.makeText(
                                        context,
                                        "No more than one Video in a post",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }

                    }
                    .show()
            }
        }

    }

    /**
     * this function adds a text editor when clicking on an empty space in the post
     */

    private fun addText() {
        val count = binding.postList.childCount
        val lastChild = binding.postList.getChildAt(count - 1)

        if (lastChild is EditText) {
            lastChild.requestFocus()
        } else {
            val param = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val text = EditText(context)
            param.setMargins(8, 8, 8, 8)
            text.layoutParams = param
            text.textSize = 15.0f
            text.background = null
            binding.postList.addView(text)
            text.requestFocus()
            text.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString() == "") {
                        if(!checkCount())
                            togglePostButton(false)
                        else
                            togglePostButton(true)
                    } else
                        togglePostButton(true)

                }
            })
        }
    }

    private fun checkCount(): Boolean {
        if(binding.postList.childCount > 1)
            return true
        else
            return false
    }

    /**
     * sets the post button with the enabled or disabled mode according to the post state
     * @param[enabled] a boolean indicates whether to enable the post button or not
     */
    private fun togglePostButton(enabled: Boolean) {
        if (enabled) {
            binding.postButtonText.setTextColor(Color.BLACK)
            binding.postButtonText.setBackgroundColor(Color.parseColor("#0277BD"))
        } else {
            binding.postButtonText.setTextColor(Color.parseColor("#BDBDBD"))
            binding.postButtonText.setBackgroundColor(Color.parseColor("#EEEEEE"))

        }
    }

    /**
     * inserts an image under the editor cursor position
     */
    private fun insertImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imageLauncher.launch(intent)

    }

    /**
     * inserts a video under the editor cursor position
     */

    private fun insertVideo() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }


    private fun addImages(clipData: ClipData) {
        val image = ImageView(context)
        image.setImageURI(clipData.getItemAt(0).uri)
        binding.postList.addView(image)

    }

    /**
     * gets the current focused text editor
     */
    private fun getActiveText(): Int {
        for (i in 0..binding.postList.childCount) {
            val child = binding.postList.getChildAt(i)
            if (child is EditText) {
                if (child.isCursorVisible && child.isFocused)
                    return i
            }
        }
        return -1
    }


}
