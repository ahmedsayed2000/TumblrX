package com.example.android.tumblrx2.addpostfragments


import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.databinding.FragmentInitialPostBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GPHMediaView
import com.giphy.sdk.ui.views.GiphyGridView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import io.github.ponnamkarthik.richlinkpreview.*
import timber.log.Timber


/**
 * this is the starter fragment in the AddPostActivity
 * @property[gestureDetector] a gesture detector for text editors touch events
 * @property[binding] the fragment binding object the holds the UI
 * @property[mediaController] mediaController to control the video in the post
 * @property[oneVideo] a boolean that indicates that the post has one video
 * @property[items] array that represents the media Type options
 * @property[textMap] a map that relates each text editor with its text size
 * @property[textSizes] array represents the text sizes
 * @property[imageLauncher] used in register the result of image returned
 * @property[videoLauncher] used in register the result of video/gif returned
 */
class InitialPostFragment : Fragment() {


    /**
     * Gesture Listener to handle text editors touch events
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            /*val sheet = TextBottomSheet()
            activity?.let { it1 -> sheet.show(it1.supportFragmentManager, "tag") }*/
            return true
        }


        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.root, InputMethodManager.SHOW_FORCED)
            binding.firstText.requestFocus()
            binding.firstText.showSoftInputOnFocus = true
            return true
        }

    }

    private lateinit var gestureDetector: GestureDetectorCompat


    private lateinit var binding: FragmentInitialPostBinding

    private lateinit var mediaController: MediaController
    private var oneVideo: Boolean = false

    private val items = arrayOf("Image(s)", "Video/Gif")

    private val textSizes = arrayOf("Regular", "Bigger", "Biggest")
    private var textMap = mutableMapOf<Int, Int>()

    private lateinit var richPreview: RichPreview
    var previewIndex = 0

    val appSdkKey = "7VZcIVNJ2mlp9FGeVAcFVY2a8NitNRVr"

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val clipData = it.data?.clipData
                // Use the uri to load the image
                if (clipData != null) {
                    Timber.d("clip data is not null")
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
                        Timber.d("last view")
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

        // giphy configuration
        context?.let { Giphy.configure(it, appSdkKey) }

        //activity?.let { GiphyDialogFragment.newInstance().show(it.supportFragmentManager, "giphy_dialog") }


        // for link previewer
        richPreview = RichPreview(object : ResponseListener {
            override fun onData(metaData: MetaData?) {
                val title = metaData?.title
                val imgUrl = metaData?.imageurl
                val description = metaData?.description
                val view = LayoutInflater.from(context).inflate(R.layout.custom_link_preview, null)

                val previewTitle = view.findViewById<TextView>(R.id.preview_title)
                val previewDesc = view.findViewById<TextView>(R.id.preview_description)
                val previewImg = view.findViewById<ImageView>(R.id.preview_image)
                val close = view.findViewById<ImageButton>(R.id.close_preview)
                close.setOnClickListener {
                    val count = binding.postList.childCount
                    for (i in 0..count) {
                        if (binding.postList.getChildAt(i) is MaterialCardView) {
                            val view: View =
                                (binding.postList.getChildAt(i)).findViewById<ImageButton>(R.id.close_preview)
                            if (view == it) {
                                binding.postList.removeViewAt(i)
                                break
                            }
                        }
                    }
                }

                previewTitle.text = title
                previewDesc.text = description

                Picasso.get().load(imgUrl).into(previewImg)

                binding.postList.addView(view, previewIndex)
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, "Please insert a correct link", Toast.LENGTH_SHORT).show()
            }
        })


        // returning the root view
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



        textMap[0] = 0
        binding.firstText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() == "") {
                    togglePostButton(false)
                } else
                    togglePostButton(true)

            }
        })

        binding.insertLink.setOnClickListener {
            val index = getActiveText()
            val view = LayoutInflater.from(context).inflate(R.layout.link_card, null)
            val preIndex: Int = if (index == -1) {
                binding.postList.addView(view)
                binding.postList.childCount - 1
            } else {
                binding.postList.addView(view, index + 1)
                index + 1
            }

            initialiseLink(view, preIndex)
        }



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

        binding.insertText.setOnClickListener {
            val index = getActiveText()
            if (index == -1) {
                addText()
            } else {
                editSelectedTextSize(index)
            }
        }

        binding.insertText.setOnLongClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.text_bottomsheet, null)
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            initialTextSheet(view)
            sheet?.setContentView(view)
            sheet?.show()
            true
        }

        binding.insertGif.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.giphy_sheet, null)
            initialiseGifView(view)
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()
        }


    }

    /**
     *
     */
    private fun initialiseGifView(view: View) {
        val grid = view.findViewById<GiphyGridView>(R.id.gifsGridView)
        grid.content = GPHContent.trendingGifs

        grid.callback = object : GPHGridCallback {
            override fun contentDidUpdate(resultCount: Int) {
                if (resultCount == -1) {
                    Toast.makeText(context, "error in rendering gifs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun didSelectMedia(media: Media) {
                val mediaView = context?.let { GPHMediaView(it) }
                mediaView?.setMedia(media, RenditionType.original)
                val param = RelativeLayout.LayoutParams(
                    100,
                    100
                )
                param.setMargins(8, 8, 10, 20)
                val index = getActiveText()
                if(index == -1) {
                    binding.postList.addView(mediaView)
                }
                else {
                    binding.postList.addView(mediaView, index + 1)
                }
            }
        }

        val textEditor = view.findViewById<EditText>(R.id.gif_text)
        val searchButton = view.findViewById<ImageButton>(R.id.search_gif)

        searchButton.setOnClickListener {
            grid.content = GPHContent.searchQuery(textEditor.text.toString(), MediaType.gif)
        }
    }


    /**
     * this function to initialise the link card
     */
    private fun initialiseLink(view: View, index: Int) {
        val close = view.findViewById<ImageButton>(R.id.link_close)
        val linkButton = view.findViewById<ImageButton>(R.id.link_button)

        linkButton.setOnClickListener {
            val text = view.findViewById<EditText>(R.id.link_url)
            previewIndex = index
            binding.postList.removeViewAt(index)
            //richPreview.getPreview(text.text.toString())
            val rich = RichLinkView(context)
            rich.setLink(text.text.toString(), object : ViewListener {
                override fun onSuccess(status: Boolean) {
                    binding.postList.addView(rich, previewIndex)
                }

                override fun onError(e: Exception?) {

                }
            })
        }

        close.setOnClickListener {
            binding.postList.removeViewAt(index)
        }
    }

    /**
     * this function initialise the text edit bottom sheet views
     */
    private fun initialTextSheet(view: View) {
        // setting buttons

        // text size button
        view.findViewById<ImageButton>(R.id.text_size)?.setOnClickListener {

            val index = getActiveText()
            if (index != -1) {
                editSelectedTextSize(index)
            }
        }

        // bold button
        view.findViewById<ImageButton>(R.id.text_bold)?.setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd
                val spanString = SpannableStringBuilder(activeEditor.text)
                spanString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString
                activeEditor.setSelection(selStart, selEnd)
            }

        }

        // italic button
        view.findViewById<ImageButton>(R.id.text_italic)?.setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText

                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)
                spanString.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString
                activeEditor.setSelection(selStart, selEnd)
            }

        }

        // strikethrough button
        view.findViewById<ImageButton>(R.id.text_strike)?.setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText

                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)
                spanString.setSpan(
                    StrikethroughSpan(),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString
                activeEditor.setSelection(selStart, selEnd)
            }
        }

        // black button
        view.findViewById<ImageButton>(R.id.black).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString

                activeEditor.setSelection(selStart, selEnd)
            }
        }

        // red button
        view.findViewById<ImageButton>(R.id.red).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString

                activeEditor.setSelection(selStart, selEnd)
            }
        }
        // orange button
        view.findViewById<ImageButton>(R.id.orange).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FF9100")),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString


                activeEditor.setSelection(selStart, selEnd)
            }
        }
        // purple button
        view.findViewById<ImageButton>(R.id.purple).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FF6200EE")),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString

                activeEditor.setSelection(selStart, selEnd)
            }
        }
        // cyan button
        view.findViewById<ImageButton>(R.id.cyan).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.CYAN),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString


                activeEditor.setSelection(selStart, selEnd)
            }
        }
        // pink button
        view.findViewById<ImageButton>(R.id.pink).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FF4081")),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString

                activeEditor.setSelection(selStart, selEnd)
            }
        }
        // green button
        view.findViewById<ImageButton>(R.id.green).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (binding.postList.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd

                val spanString = SpannableStringBuilder(activeEditor.text)

                spanString.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )

                activeEditor.text = spanString

                activeEditor.setSelection(selStart, selEnd)
            }
        }


    }


    /**
     * this function adds a text editor when clicking on an empty space in the post or on the insert Text Button
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
            textMap[count] = 0
            text.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString() == "") {
                        togglePostButton(false)
                    } else
                        togglePostButton(true)

                }
            })
        }
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
     * @return the index of the focused text editor if no editor is focused returns -1
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

    private fun editSelectedTextSize(index: Int) {
        val editor = (binding.postList.getChildAt(index)) as EditText
        textMap[index] = (textMap[index]!! + 1) % 3

        val selStart = editor.selectionStart
        val selEnd = editor.selectionEnd
        when (textSizes[textMap[index]!!]) {
            "Regular" -> {
                if (editor.selectionStart != -1) {
                    val spanString = SpannableStringBuilder(editor.text)
                    spanString.setSpan(
                        AbsoluteSizeSpan(15, true),
                        editor.selectionStart,
                        editor.selectionEnd,
                        0
                    )
                    editor.text = spanString
                    editor.setSelection(selStart, selEnd)
                } else {
                    editor.textSize = 15.0f
                    editor.selectAll()
                }

                Toast.makeText(context, "Regular", Toast.LENGTH_SHORT).show()
            }
            "Bigger" -> {
                if (editor.selectionStart != -1) {
                    val spanString = SpannableStringBuilder(editor.text)
                    spanString.setSpan(
                        AbsoluteSizeSpan(20, true),
                        editor.selectionStart,
                        editor.selectionEnd,
                        0
                    )
                    editor.text = spanString
                    editor.setSelection(selStart, selEnd)
                } else {
                    editor.textSize = 20.0f
                    editor.selectAll()
                }

                Toast.makeText(context, "Bigger", Toast.LENGTH_SHORT).show()
            }
            "Biggest" -> {
                if (editor.selectionStart != -1) {
                    val spanString = SpannableStringBuilder(editor.text)
                    spanString.setSpan(
                        AbsoluteSizeSpan(25, true),
                        editor.selectionStart,
                        editor.selectionEnd,
                        0
                    )
                    editor.text = spanString
                    editor.setSelection(selStart, selEnd)
                } else {
                    editor.textSize = 25.0f
                    editor.selectAll()
                }

                Toast.makeText(context, "Biggest", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
