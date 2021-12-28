package com.example.android.tumblrx2.addpost.addpostfragments.postobjects

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import com.example.android.tumblrx2.addpost.addpostfragments.AddPostViewModel
import com.example.android.tumblrx2.R
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.views.GPHMediaView
import io.github.ponnamkarthik.richlinkpreview.RichLinkView
import io.github.ponnamkarthik.richlinkpreview.ViewListener

class AddPostAdapter : ArrayAdapter<AddPostItem> {


    private inner class TextHolder(var text: TextView, var item: AddPostItem) {
    }

    private val textHolderList = mutableListOf<TextHolder>()


    private var postViewModel: AddPostViewModel

    val ITEM_TEXT = 1
    val ITEM_IMAGE = 2
    val ITEM_VIDEO = 3
    val ITEM_LINK = 4
    val ITEM_LINK_PREVIEW = 5
    private val ITEM_GIPH = 6

    private var listOfItems: MutableList<AddPostItem>

    constructor(
        context: Context,
        res: Int,
        list: MutableList<AddPostItem>,
        viewModel: AddPostViewModel
    ) : super(
        context,
        res,
        list
    ) {
        listOfItems = list
        postViewModel = viewModel
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val listItem = listOfItems.get(position)
        val type = getItemViewType(position)
        var view: View? = null


        if (convertView == null) {
            when (type) {
                ITEM_TEXT -> {
                    view = EditText(context)
                    view?.let { initText(it, listItem.textGestureDetector!!, listItem) }
                    val holder = TextHolder(view, listItem)
                    view.tag = holder
                    textHolderList.add(holder)

                }
                ITEM_IMAGE -> {
                    view = ImageView(context)
                    view?.let { initImage(it, listItem.content) }
                }
                ITEM_VIDEO -> {
                    view = VideoView(context)
                    view?.let {
                        listItem.mediaController?.let { it1 ->
                            initVideo(
                                it,
                                it1,
                                listItem.content
                            )
                        }
                    }
                }

                ITEM_LINK -> {
                    view = LayoutInflater.from(context).inflate(R.layout.link_card, parent, false)
                    view?.let { initLink(it, listItem) }
                }

                ITEM_LINK_PREVIEW -> {
                    view = RichLinkView(context)
                    initPreview(view, listItem.content)
                }

                ITEM_GIPH -> {
                    view = GPHMediaView(context)
                    initGiph(view, listItem.giphMedia)
                }
            }
        } else {
            when (type) {
                ITEM_TEXT -> {
                    val index = searchHolder(listItem)
                    if (index != -1) {
                        view = textHolderList.get(index).text
                    } else {
                        view = EditText(context)
                        view?.let {
                            initText(it, listItem.textGestureDetector!!, listItem)
                        }
                        val holder = TextHolder(view, listItem)
                        view.tag = holder
                        textHolderList.add(holder)
                    }
                }
                ITEM_IMAGE -> {
                    if (convertView is ImageView) {
                        initImage(convertView, listItem.content)
                        view = convertView
                    } else {
                        view = ImageView(context)
                        view?.let { initImage(it, listItem.content) }
                    }
                }
                ITEM_VIDEO -> {
                    if (convertView is VideoView) {
                        initVideo(convertView, listItem.mediaController!!, listItem.content)
                        view = convertView
                    } else {
                        view = VideoView(context)
                        view?.let {
                            listItem.mediaController?.let { it1 ->
                                initVideo(
                                    it,
                                    it1,
                                    listItem.content
                                )
                            }
                        }
                    }
                }

                ITEM_LINK -> {
                    if (!(convertView is VideoView) && !(convertView is ImageView) && !(convertView is EditText) && !(convertView is RichLinkView) && !(convertView is GPHMediaView)) {
                        initLink(convertView, listItem)
                        view = convertView
                    } else {
                        view =
                            LayoutInflater.from(context).inflate(R.layout.link_card, parent, false)
                        view?.let { initLink(it, listItem) }
                    }
                }

                ITEM_LINK_PREVIEW -> {
                    if (convertView is RichLinkView) {
                        initPreview(convertView, listItem.content)
                        view = convertView
                    } else {
                        view = RichLinkView(context)
                        initPreview(view, listItem.content)
                    }
                }
                ITEM_GIPH -> {
                    if (convertView is GPHMediaView) {
                        initGiph(convertView, listItem.giphMedia)
                        view = convertView
                    } else {
                        view = GPHMediaView(context)
                        initGiph(view, listItem.giphMedia)
                    }
                }
            }
        }


        return view!!
    }

    private fun initGiph(view: GPHMediaView, giphMedia: Media?) {
        view.setMedia(giphMedia, RenditionType.original)
    }

    private fun initPreview(view: RichLinkView, content: String) {
        view.setLink(content, object : ViewListener {
            override fun onSuccess(status: Boolean) {
                //Toast.makeText(context, "correct link", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception?) {
                //Toast.makeText(context, "incorrect link", Toast.LENGTH_SHORT).show()
                e?.printStackTrace()
            }
        })

    }

    override fun getItemViewType(position: Int): Int {
        return listOfItems.get(position).type
    }

    override fun getViewTypeCount(): Int {
        return 7
    }

    private fun initText(view: EditText, detector: GestureDetectorCompat, item: AddPostItem) {
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        param.setMargins(8, 8, 8, 8)

        view.textSize = 15.0f
        view.background = null
        view.layoutParams = param
        view.hint = "add something you,d like"
        view.setText(item.content)

        view.setOnTouchListener { view, motionEvent ->
            detector.onTouchEvent(motionEvent)
        }

        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                item.content = p0.toString()
                if (view.text.toString() == "") {
                    if (listOfItems.size == 1)
                        postViewModel.togglePostButton.value = false
                } else
                    postViewModel.togglePostButton.value = true
            }
        })

    }

    private fun initImage(view: ImageView, content: String) {
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(8, 8, 8, 8)

        val uri = Uri.parse(content)
        view.setImageURI(uri)
        view.layoutParams = param
        view.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun initVideo(view: VideoView, mediaController: MediaController, content: String) {
        /*val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(8, 8, 8, 8)*/

        val uri = Uri.parse(content)
        mediaController.setMediaPlayer(view)
        mediaController.setAnchorView(view)
        view.setMediaController(mediaController)
        view.setVideoURI(uri)
        //view.setVideoPath(uri.path)

        view.setOnCompletionListener {
            view.start()
        }

        /*view.setVideoURI(uri)
        view.start()*/
    }

    private fun initLink(view: View, item: AddPostItem) {
        val close = view.findViewById<ImageButton>(R.id.link_close)
        val linkButton = view.findViewById<ImageButton>(R.id.link_button)

        val text = view.findViewById<EditText>(R.id.link_url)
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                item.content = p0.toString()
            }
        })

        text.setText(item.content)


        linkButton.setOnClickListener {
            val text = view.findViewById<EditText>(R.id.link_url)
            val linkString = text.text.toString()
            val position = searchForLink(item)
            if (position != -1) {
                listOfItems.removeAt(position)
                listOfItems.add(position, AddPostItem(5, linkString))
            }
        }

        close.setOnClickListener {
            val position = searchForLink(item)
            if (position != -1) {
                listOfItems.removeAt(position)
            }
            this.notifyDataSetChanged()
        }
    }

    private fun searchHolder(item: AddPostItem): Int {
        for (i in 0 until textHolderList.size) {
            if (textHolderList.get(i).item === item)
                return i
        }

        return -1
    }

    private fun searchForLink(item: AddPostItem): Int {
        if (listOfItems.contains(item)) {
            return listOfItems.indexOf(item)
        }
        return -1

    }

}

/*
val index = searchHolder(listItem)
                    if (index != -1) {
                        view = textHolderList.get(index).text
                    }
                    else {
                        view = EditText(context)
                        view?.let {
                            initText(it, listItem.textGestureDetector!!, listItem)
                        }
                        val holder = TextHolder(view, listItem)
                        view.tag = holder
                        textHolderList.add(holder)
                    }
 */

/*
if (convertView == null) {
            when (type) {
                ITEM_TEXT -> {
                    view = EditText(context)
                    view?.let { initText(it, listItem.textGestureDetector!!, listItem) }
                    val holder = TextHolder(view, listItem)
                    view.tag = holder
                    textHolderList.add(holder)

                }
                ITEM_IMAGE -> {
                    view = ImageView(context)
                    view?.let { initImage(it, listItem.content) }
                }
                ITEM_VIDEO -> {
                    view = VideoView(context)
                    view?.let {
                        listItem.mediaController?.let { it1 ->
                            initVideo(
                                it,
                                it1,
                                listItem.content
                            )
                        }
                    }
                }

                ITEM_LINK -> {
                    view = LayoutInflater.from(context).inflate(R.layout.link_card, parent, false)
                    view?.let { initLink(it, listItem) }
                }

                ITEM_LINK_PREVIEW -> {
                    view = RichLinkView(context)
                    initPreview(view, listItem.content)
                }

                ITEM_GIPH -> {
                    view = GPHMediaView(context)
                    initGiph(view, listItem.giphMedia)
                }
            }
        } else {
            when (type) {
                ITEM_TEXT -> {
                    val index = searchHolder(listItem)
                    if (index != -1) {
                        view = textHolderList.get(index).text
                        view.requestFocus()
                    }
                    else {
                        view = EditText(context)
                        view?.let {
                            initText(it, listItem.textGestureDetector!!, listItem)
                        }
                        val holder = TextHolder(view, listItem)
                        view.tag = holder
                        textHolderList.add(holder)
                    }
                }
                ITEM_IMAGE -> {
                    if (convertView is ImageView) {
                        initImage(convertView, listItem.content)
                        view = convertView
                    } else {
                        view = ImageView(context)
                        view?.let { initImage(it, listItem.content) }
                    }
                }
                ITEM_VIDEO -> {
                    if (convertView is VideoView) {
                        initVideo(convertView, listItem.mediaController!!, listItem.content)
                        view = convertView
                    } else {
                        view = VideoView(context)
                        view?.let {
                            listItem.mediaController?.let { it1 ->
                                initVideo(
                                    it,
                                    it1,
                                    listItem.content
                                )
                            }
                        }
                    }
                }

                ITEM_LINK -> {
                    if (!(convertView is EditText) && !(convertView is ImageView) && !(convertView is VideoView) && !(convertView is RichLinkView) && !(convertView is GPHMediaView)) {
                        view = convertView
                    } else {
                        view =
                            LayoutInflater.from(context).inflate(R.layout.link_card, parent, false)
                        view?.let { initLink(it, listItem) }
                    }
                }

                ITEM_LINK_PREVIEW -> {
                    if (convertView is RichLinkView) {
                        initPreview(convertView, listItem.content)
                        view = convertView
                    } else {
                        view = RichLinkView(context)
                        initPreview(view, listItem.content)
                    }
                }
                ITEM_GIPH -> {
                    if (convertView is GPHMediaView) {
                        initGiph(convertView, listItem.giphMedia)
                        view = convertView
                    } else {
                        view = GPHMediaView(context)
                        initGiph(view, listItem.giphMedia)
                    }
                }
            }
        }


        return view!!
    }
 */
