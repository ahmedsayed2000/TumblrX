package com.example.android.tumblrx2.addpostfragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.databinding.FragmentInitialPostBinding
import android.content.ClipData
import android.widget.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class InitialPostFragment : Fragment() {

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
                    val image = ImageView(context)
                    image.setImageURI(it.data?.data)
                    val index = getActiveText()
                    if (index == -1) {
                        Log.d("Initial fragment", "last view")
                        binding.postList.addView(image)
                    } else {
                        Log.d("Initial fragment", "view at index ${index + 1}")
                        binding.postList.addView(image, index + 1)
                    }
                }

            }
        }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                val video = binding.postVideo
                mediaController.setMediaPlayer(video)
                mediaController.setAnchorView(video)
                video.setMediaController(mediaController)
                video.setVideoURI(uri)
                video.visibility = View.VISIBLE
                video.start()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_initial_post, container, false)
        initialViews()

        mediaController = MediaController(context)

        return binding.root
    }

    private fun initialViews() {


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

    private fun insertImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imageLauncher.launch(intent)

    }

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
