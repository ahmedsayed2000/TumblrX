package com.example.android.tumblrx2

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.tumblrx2.postobjects.AddPostItem

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


    // the activity context
    private lateinit var context: Context

    // the live data of Post List items
    var postListItems: MutableLiveData<MutableList<AddPostItem>> = MutableLiveData()

    // a live data boolean for the post button
    var togglePostButton: MutableLiveData<Boolean> = MutableLiveData()

    // the post list
    private lateinit var postListView: ListView

    // text editors gesture detector
    private lateinit var gestureDetector: GestureDetectorCompat

    // objects concerning the text sizes in the editors
    private val textSizes = arrayOf("Regular", "Bigger", "Biggest")
    private var textMap = mutableMapOf<Int, Int>()


    init {
        Log.d("initial fragment", " view model created")
        // init the post item list
        val list = mutableListOf<AddPostItem>()
        postListItems.value = list

        // init the toggle post button
        togglePostButton.value = false
    }

    fun initViews(view: ListView, context: Context) {
        // init the context and needed objects
        this.context = context
        gestureDetector = GestureDetectorCompat(this.context, GestureListener())

        // adding and initializing the first edit text
        val item = AddPostItem(1, "")
        item.textGestureDetector = this.gestureDetector
        postListItems.value?.add(item)
        textMap[0] = 0

        // getting the post list
        postListView = view
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
        val count = postListView.childCount
        val lastChild = postListView.getChildAt(count - 1)

        // checking if the last child is an Edit text
        if (lastChild is EditText) {
            // requesting the focus in order not to add another edit text
            lastChild.requestFocus()
        } else {
            // adding a new text editor to the list
            val item = AddPostItem(1, "")
            item.textGestureDetector = gestureDetector
            postListItems.value?.add(item)
            textMap[count] = 0
        }

    }


    private fun editSelectedTextSize(index: Int) {
        // getting the text editor to increase its text size
        val editor = (postListView.getChildAt(index)) as EditText

        // incrementing the text size
        textMap[index] = (textMap[index]!! + 1) % 3


        val selStart = editor.selectionStart
        val selEnd = editor.selectionEnd

        // getting the current size to update
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
                }
                //Toast.makeText(context, "Regular", Toast.LENGTH_SHORT).show()
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

                //Toast.makeText(context, "Bigger", Toast.LENGTH_SHORT).show()
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

                //Toast.makeText(context, "Biggest", Toast.LENGTH_SHORT).show()
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

                activeEditor.text = spanString
                activeEditor.setSelection(selStart, selEnd)
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
            }
        }
    }

}