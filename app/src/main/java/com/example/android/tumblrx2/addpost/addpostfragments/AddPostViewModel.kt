package com.example.android.tumblrx2.addpost.addpostfragments

import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.tumblrx2.BlogSearchList
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.addpost.addpostfragments.postobjects.*
import com.example.android.tumblrx2.repository.AddPostRepository
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GPHMediaView
import com.giphy.sdk.ui.views.GiphyGridView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.toHexString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class AddPostViewModel : ViewModel() {


    /**
     * Gesture Listener to handle text editors touch events
     */
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            val index = getActiveText()
            if (index != -1) {
                val text = (postListView.getChildAt(index)) as EditText
                text.setSelection(0, text.length())
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            val index = getActiveText()
            if (index != -1) {
                val text = (postListView.getChildAt(index)) as EditText
                text.setSelection(0, text.length())
            }
        }

    }

    data class SizeOfText(val item: AddPostItem, var size: Int)

    private var listOfSizes = mutableListOf<SizeOfText>()

    // list of post objects
    val fileList = mutableListOf<MultipartBody.Part>()
    val contentList = mutableListOf<MultipartBody.Part>()
    val tagsList = mutableListOf<MultipartBody.Part>()


    // the activity context
    private lateinit var context: Context

    // the live data of Post List items
    var postListItems: MutableLiveData<MutableList<AddPostItem>> = MutableLiveData()

    // a live data boolean for the post button
    var togglePostButton: MutableLiveData<Boolean> = MutableLiveData()

    // the post list
    private lateinit var postListView: ListView

    // the post list adapter
    lateinit var adapter: AddPostAdapter

    // text editors gesture detector
    private lateinit var gestureDetector: GestureDetectorCompat

    // the video media controller
    lateinit var mediaController: MediaController

    // objects concerning the text sizes in the editors
    private val textSizes = arrayOf("Regular", "Bigger", "Biggest")
    private var textMap = mutableMapOf<AddPostItem, Int>()
    private var boldList = mutableListOf<AddPostItem>()
    private var italicList = mutableListOf<AddPostItem>()
    private var strikeList = mutableListOf<AddPostItem>()
    private var colorMap = mutableMapOf<AddPostItem, String>()

    // first text
    private val firstItem = AddPostItem(1, "")

    // for the tags sheet
    var selectedBlogs = MutableLiveData<MutableList<String>>()
    var tagsCount = MutableLiveData<Int>()
    var searchedBlogs: MutableList<BlogSearch>? = null

    // to finish the post
    var finishPost = MutableLiveData<Boolean>()

    // the repository
    val repo = AddPostRepository()

    init {
        Log.d("initial fragment", " view model created")
        // init the post item list
        val list = mutableListOf<AddPostItem>()
        postListItems.value = list

        // adding and initializing the first edit text
        postListItems.value?.add(firstItem)
        textMap[firstItem] = 0
        listOfSizes.add(SizeOfText(firstItem, 0))

        // init the toggle post button
        togglePostButton.value = false

        // init the selected tags
        selectedBlogs.value = mutableListOf()
        tagsCount.value = selectedBlogs.value!!.size
        Log.d("tags count", "count is ${tagsCount.value}")

    }

    fun initViews(view: ListView, context: Context) {

        // context and needed objects
        this.context = context
        gestureDetector = GestureDetectorCompat(this.context, GestureListener())
        mediaController = MediaController(context)

        // initialise the first element
        firstItem.textGestureDetector = this.gestureDetector

        Log.d("initial fragment", "first text size = ${textMap[firstItem]}")

        // init the list view
        postListView = view

        // print color
        Log.d("initial fragment", "green is ${Color.GREEN.toHexString()}")

    }

    private fun getActiveText(): Int {
        val size = postListView.childCount
        for (i in 0 until size) {
            if (postListView.getChildAt(i) is EditText) {
                val text = (postListView.getChildAt(i)) as EditText
                if (text.isFocused)
                    return i
            }
        }
        return -1
    }

    private fun togglePostButton(toggle: Boolean) {
        // if true the post button is blue else gray
        togglePostButton.value = toggle
    }

    fun addImage(uri: Uri) {
        // getting the index to add to
        val index = getActiveText()
        if (index == -1) {
            Log.d("initial fragment", "last view")
            postListItems.value?.add(AddPostItem(2, uri.toString()))
        } else {
            Log.d("initial fragment", "view at index ${index + 1}")
            postListItems.value?.add(index + 1, AddPostItem(2, uri.toString()))
        }
        togglePostButton(true)
    }

    fun addImages(clipData: ClipData) {
        // getting the index to add to
        val index = getActiveText()
        var addIndex = 0
        if (index == -1)
            addIndex = postListView.childCount
        else
            addIndex = index + 1

        for (i in 0 until clipData.itemCount) {
            val uriString = clipData.getItemAt(i).uri.toString()
            postListItems.value?.add(addIndex + i, AddPostItem(2, uriString))
        }
        togglePostButton(true)
    }

    fun addText() {
        Log.d("initial fragment", "add text")
        val count = postListView.childCount
        val lastChild = postListView.getChildAt(count - 1)

        // checking if the last child is an Edit text
        if (lastChild is EditText) {
            // requesting the focus in order not to add another edit text
            Log.d("initial fragment", "request the focus")
            lastChild.requestFocus()
            lastChild.isCursorVisible = true
            //adapter.notifyDataSetChanged()
        } else {
            // adding a new text editor to the list
            val item = AddPostItem(1, "")
            item.textGestureDetector = gestureDetector
            postListItems.value?.add(item)
            //textMap[item] = 0
            listOfSizes.add(SizeOfText(item, 0))
        }

    }


    private fun editSelectedTextSize(index: Int) {
        // getting the text editor to increase its text size
        Log.d("initial fragment", "before area")
        val editor = (postListView.getChildAt(index)) as EditText

        val item = (postListItems.value?.get(index)) as AddPostItem

        Log.d("initial fragment", "index = ${index}")

        // incrementing the text size
        //textMap[item] = (textMap[item]!! + 1) % 3
        //textMap[(postListItems.value!!.get(index))] = (textMap[(postListItems.value!!.get(index))]!! + 1) % 3

        var textSize: SizeOfText? = null
        for (x in listOfSizes) {
            if (x.item === item)
                textSize = x
        }

        textSize?.size = (textSize?.size!! + 1) % 3

        Log.d("initial fragment", "before area with map = ${textSize.size}")


        val selStart = editor.selectionStart
        val selEnd = editor.selectionEnd

        // getting the current size to update
        when (textSizes[textSize.size]) {
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
                }

                Toast.makeText(context, "Biggest", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun insertOrEditText() {
        val index = getActiveText()
        if (index == -1) {
            addText()
        } else {
            editSelectedTextSize(index)
        }
    }

    fun insertLink() {
        val index = getActiveText()
        if (index == -1) {
            // adding to list at last position
            postListItems.value?.add(AddPostItem(4, ""))
        } else {
            // adding to the list at a specific position
            postListItems.value?.add(index + 1, AddPostItem(4, ""))
        }
    }


    fun initTextBottomSheet(view: View) {
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
                val activeEditor = (postListView.getChildAt(index)) as EditText
                val selStart = activeEditor.selectionStart
                val selEnd = activeEditor.selectionEnd
                val spanString = SpannableStringBuilder(activeEditor.text)
                spanString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    activeEditor.selectionStart,
                    activeEditor.selectionEnd,
                    0
                )
                val item = (postListItems.value?.get(index)) as AddPostItem

                activeEditor.text = spanString
                activeEditor.setSelection(selStart, selEnd)
                //styleMap[item] = 0
                boldList.add(item)
            }

        }

        // italic button
        view.findViewById<ImageButton>(R.id.text_italic)?.setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText

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
                val item = (postListItems.value?.get(index)) as AddPostItem
                //styleMap[item] = 1
                italicList.add(item)
            }

        }

        // strikethrough button
        view.findViewById<ImageButton>(R.id.text_strike)?.setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText

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
                val item = (postListItems.value?.get(index)) as AddPostItem
                //styleMap[item] = 2
                strikeList.add(item)

            }
        }

        // black button
        view.findViewById<ImageButton>(R.id.black).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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

                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#000000"
            }
        }

        // red button
        view.findViewById<ImageButton>(R.id.red).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#d50000"
            }
        }
        // orange button
        view.findViewById<ImageButton>(R.id.orange).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#ff9100"
            }
        }
        // purple button
        view.findViewById<ImageButton>(R.id.purple).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#df00f9"
            }
        }
        // cyan button
        view.findViewById<ImageButton>(R.id.cyan).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#00bcd4"
            }
        }
        // pink button
        view.findViewById<ImageButton>(R.id.pink).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#ff4081"
            }
        }
        // green button
        view.findViewById<ImageButton>(R.id.green).setOnClickListener {
            val index = getActiveText()
            if (index != -1) {
                val activeEditor = (postListView.getChildAt(index)) as EditText
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
                val item = (postListItems.value?.get(index)) as AddPostItem
                colorMap[item] = "#00c853"


            }
        }
    }

    fun initialiseGifView(view: View) {
        val grid = view.findViewById<GiphyGridView>(R.id.gifsGridView)
        grid.content = GPHContent.trendingGifs

        grid.callback = object : GPHGridCallback {
            override fun contentDidUpdate(resultCount: Int) {
                if (resultCount == -1) {
                    Toast.makeText(context, "error in rendering gifs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun didSelectMedia(media: Media) {

                // new gif item to be added
                val item = AddPostItem(6, "")
                item.giphMedia = media

                val index = getActiveText()
                // adding the gif at the correct position
                if (index == -1) {
                    postListItems.value?.add(item)
                    Log.d("initial fragment", "last view")
                } else {
                    postListItems.value?.add(index + 1, item)
                }
                togglePostButton(true)
                adapter.notifyDataSetChanged()
            }
        }


        val textEditor = view.findViewById<EditText>(R.id.gif_text)
        val searchButton = view.findViewById<ImageButton>(R.id.search_gif)

        // setting the search button click listener
        searchButton.setOnClickListener {
            grid.content = GPHContent.searchQuery(textEditor.text.toString(), MediaType.gif)
        }
    }

    fun addVideo(uri: Uri) {
        val real = getRealPath(uri.toString())

        val item = AddPostItem(3, real)
        item.mediaController = mediaController

        val index = getActiveText()
        if (index == -1) {
            postListItems.value?.add(item)
        } else {
            postListItems.value?.add(index + 1, item)
        }
        Log.d("Post view model", "the video is added")
        Log.d("Post view model", "type at 2 is ${postListItems.value!![1].type}")
        togglePostButton(true)

        //adapter.notifyDataSetChanged()
    }

    fun postToBlog(id: String, postType: String) {
        //prepareList()
        prepareTags()

        if (postListItems.value?.size!! > 1 || firstItem.content != "") {
            prepareList()
            repo?.postToBlog(context, this, id, postType, contentList, fileList, tagsList)
        } else {
            Toast.makeText(context, "no items to post", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepareTags() {
        val index = 0
        for (tag in selectedBlogs.value!!) {
            val part = MultipartBody.Part.createFormData("tags[${index}]", tag)
            tagsList.add(part)
        }
    }


    private fun prepareList() {

        var index = 0
        for (item in postListItems.value!!) {
            when (item.type) {
                1 -> {
                    // text
                    if (item.content != "") {
                        prepareTextPart(index)
                    }
                }
                2 -> {
                    // the image item
                    prepareImagePart(item.content, index)
                    //tryImage(item.content, index)
                }
                3 -> {
                    prepareVideoPart(item.content, index)
                }
                4 -> {
                    //prepareLinkPart(index, item)
                }
                5 -> {
                    preparePreviewPart(index, item)
                }
                6 -> {
                    prepareGifPart(index, item)
                }
            }

            index = index + 1
        }


    }


    private fun preparePreviewPart(index: Int, item: AddPostItem) {
        val part = MultipartBody.Part.createFormData("content[${index}][type]", "link")
        val part1 = MultipartBody.Part.createFormData("content[${index}][url]", item.content)

        contentList.add(part)
        contentList.add(part1)

    }

    private fun prepareTextPart(index: Int) {

        val item = postListItems.value?.get(index) as AddPostItem
        val part = MultipartBody.Part.createFormData("content[${index}][type]", "text")
        val part1 = MultipartBody.Part.createFormData("content[${index}][text]", item.content)

        contentList.add(part)
        contentList.add(part1)
        var counter = 0
        if (colorMap.contains(item)) {
            val part = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][type]",
                "color"
            )
            val part1 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][start]",
                "0"
            )
            val part2 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][end]",
                "${item.content.length - 1}"
            )
            val part3 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][hex]",
                colorMap[item]!!
            )

            contentList.add(part)
            contentList.add(part1)
            contentList.add(part2)
            contentList.add(part3)
            counter = counter + 1

            Log.d("view model", "color added")
        }
        if (boldList.contains(item)) {
            val part = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][type]",
                "bold"
            )
            val part1 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][start]",
                "0"
            )
            val part2 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][end]",
                "${item.content.length - 1}"
            )

            contentList.add(part)
            contentList.add(part1)
            contentList.add(part2)
            counter = counter + 1

            Log.d("view model", "bold added")
        }
        if (italicList.contains(item)) {
            val part = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][type]",
                "italic"
            )
            val part1 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][start]",
                "0"
            )
            val part2 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][end]",
                "${item.content.length - 1}"
            )

            contentList.add(part)
            contentList.add(part1)
            contentList.add(part2)
            counter = counter + 1

            Log.d("view model", "italic added")
        }
        if (strikeList.contains(item)) {
            val part = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][type]",
                "strikethrough"
            )
            val part1 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][start]",
                "0"
            )
            val part2 = MultipartBody.Part.createFormData(
                "content[${index}][formatting][${counter}][end]",
                "${item.content.length - 1}"
            )
            contentList.add(part)
            contentList.add(part1)
            contentList.add(part2)
            counter = counter + 1


            Log.d("view model", "strike added")
        }


    }

    private fun prepareGifPart(index: Int, item: AddPostItem) {

        val mediaView = postListView.getChildAt(index) as GPHMediaView
        val url = mediaView.media?.url

        val part = MultipartBody.Part.createFormData(
            "content[${index}][type]",
            "image"
        )
        val part1 = MultipartBody.Part.createFormData(
            "content[${index}][media]",
            "image/gif"
        )
        val part2 = MultipartBody.Part.createFormData(
            "content[${index}][url]",
            url!!
        )

        contentList.add(part)
        contentList.add(part1)
        contentList.add(part2)
    }

    private fun prepareVideoPart(path: String, index: Int) {
        val file = File(path)

        val extention = path.substring(path.lastIndexOf(".") + 1)
        Log.d("extention is", extention)

        if (extention.lowercase() == "mp4") {
            val requestBody = file.asRequestBody("video/mp4".toMediaTypeOrNull())

            val part = MultipartBody.Part.createFormData("video" + index, file.name, requestBody)

            fileList.add(part)

            val part1 = MultipartBody.Part.createFormData("content[$index][type]", "video")
            val part2 =
                MultipartBody.Part.createFormData("content[$index][identifier]", "video${index}")

            contentList.add(part1)
            contentList.add(part2)
        } else if (extention.lowercase() == "gif") {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

            val part = MultipartBody.Part.createFormData("gif" + index, file.name, requestBody)

            fileList.add(part)

            val part1 = MultipartBody.Part.createFormData("content[$index][type]", "image")
            val part2 =
                MultipartBody.Part.createFormData("content[$index][identifier]", "gif${index}")

            contentList.add(part1)
            contentList.add(part2)
        }
    }


    private fun prepareImagePart(uriString: String, index: Int) {
        val uri = (Uri.parse(uriString)) as Uri
        Log.d("image path", "${uriString}")

        val real = getRealPath(uriString)
        Log.d("real image path", "${real}")
        val file = File(real)

        val extention = real.substring(real.lastIndexOf(".") + 1)
        Log.d("extention is", extention)
        val requestBody = file.asRequestBody("image/${extention}".toMediaTypeOrNull())

        val part = MultipartBody.Part.createFormData("image" + index, file.name, requestBody)

        fileList.add(part)

        val part1 = MultipartBody.Part.createFormData("content[$index][type]", "image")
        val part2 =
            MultipartBody.Part.createFormData("content[$index][identifier]", "image${index}")

        contentList.add(part1)
        contentList.add(part2)
    }



    private fun getRealPath(uriString: String): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(Uri.parse(uriString), proj, null, null, null)
        var result = ""
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
            return result
        }
        return result
    }


    fun initTagSheet(view: View) {
        val button = view.findViewById<Button>(R.id.done_button)
        val topChipGroup = view.findViewById<ChipGroup>(R.id.top_chipGroup)
        val bottomChipGroup = view.findViewById<ChipGroup>(R.id.bottom_chip_group)
        val searchText = view.findViewById<EditText>(R.id.search_tags)

        initTopChipGroup(topChipGroup, bottomChipGroup)

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                Log.d("searh string", p0.toString())
                if (p0.toString() != "") {

                    val call = repo.searchBlogs(p0.toString())

                    call.enqueue(object : Callback<BlogSearchList> {
                        override fun onResponse(
                            call: Call<BlogSearchList>,
                            response: Response<BlogSearchList>
                        ) {
                            /*Toast.makeText(
                                context,
                                "response happened with status ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()*/
                            Log.d("request", response.message())

                            searchedBlogs = response.body()!!.blogs
                            fillChipGroup(bottomChipGroup, topChipGroup)
                        }

                        override fun onFailure(call: Call<BlogSearchList>, t: Throwable) {

                            //Toast.makeText(context, "response failed", Toast.LENGTH_SHORT).show()
                            Log.d("request failed", t.message.toString()!!)
                        }
                    })

                }

            }
        })



        button.setOnClickListener {

        }


    }

    private fun initTopChipGroup(topGroup: ChipGroup, bottomGroup: ChipGroup) {
        val list = selectedBlogs.value
        for (chip in list!!) {
            addTopChip(chip, topGroup, bottomGroup, false)
        }
    }

    private fun fillChipGroup(bottomChipGroup: ChipGroup, topChipGroup: ChipGroup) {

        if (searchedBlogs != null) {
            Log.d(
                "view model",
                "the search Blogs list is not empty with size = ${searchedBlogs!!.size}"
            )
            for (blog in searchedBlogs!!) {

                addBottomChip("#" + blog.handle, topChipGroup, bottomChipGroup)

            }

        }


    }

    fun addBottomChip(text: String, topGroup: ChipGroup, bottomGroup: ChipGroup) {
        val chip = Chip(context)

        chip.text = text
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chip.layoutParams = param
        chip.setOnClickListener {
            addTopChip(chip.text.toString(), topGroup, bottomGroup, true)
            bottomGroup.removeView(chip)
        }

        bottomGroup.addView(chip)
    }

    fun addTopChip(text: String, topGroup: ChipGroup, bottomGroup: ChipGroup, add: Boolean) {
        val chip = Chip(context)

        chip.text = text
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chip.layoutParams = param

        topGroup.isSingleSelection = true


        chip.setOnClickListener {
            chip.isChecked = true
            chip.isCloseIconVisible = true
        }

        chip.setOnCloseIconClickListener {
            val text = chip.text.toString()
            addBottomChip(chip.text.toString(), topGroup, bottomGroup)
            topGroup.removeView(chip)

            if (selectedBlogs.value!!.contains(text)) {
                selectedBlogs.value!!.remove(text)
                tagsCount.value = selectedBlogs.value!!.size
                Log.d("tag removed", "count is ${tagsCount.value}")
            }

        }

        topGroup.addView(chip)

        // updating the selected tags
        if (add) {
            selectedBlogs.value!!.add(chip.text.toString())

            tagsCount.value = selectedBlogs.value!!.size
            Log.d("tag added", "count is ${tagsCount.value}")
        }


    }


}


