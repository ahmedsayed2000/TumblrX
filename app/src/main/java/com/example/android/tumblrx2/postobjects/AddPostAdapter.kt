package com.example.android.tumblrx2.postobjects

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import com.example.android.tumblrx2.R
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.views.GPHMediaView
import io.github.ponnamkarthik.richlinkpreview.RichLinkView
import io.github.ponnamkarthik.richlinkpreview.ViewListener
import timber.log.Timber

class AddPostAdapter : ArrayAdapter<AddPostItem> {

    val ITEM_TEXT = 1
    val ITEM_IMAGE = 2
    val ITEM_VIDEO = 3
    val ITEM_LINK = 4
    val ITEM_LINK_PREVIEW = 5
    private val ITEM_GIPH = 6

    private var listOfItems: MutableList<AddPostItem>

    constructor(context: Context, res: Int, list: MutableList<AddPostItem>) : super(
        context,
        res,
        list
    ) {
        listOfItems = list
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val listItem = listOfItems.get(position)
        val type = getItemViewType(position)
        var view: View? = null

        if (convertView == null) {
            when (type) {
                ITEM_TEXT -> {
                    view = EditText(context)
                    view?.let { initText(it, listItem.textGestureDetector!!) }
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
                    view?.let { initLink(it, position) }
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
                    if (convertView is EditText) {
                        view = convertView
                    } else {
                        view = EditText(context)
                        view?.let { initText(it, listItem.textGestureDetector!!) }
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
                        view?.let { initLink(it, position) }
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
            //view = convertView
        }


        return view!!
    }

    private fun initGiph(view: GPHMediaView, giphMedia: Media?) {
        view.setMedia(giphMedia, RenditionType.original)

        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(8, 8, 10, 20)

        view.layoutParams = param

        this.notifyDataSetChanged()
    }

    private fun initPreview(view: RichLinkView, content: String) {
        view.setLink(content, object : ViewListener {
            override fun onSuccess(status: Boolean) {
                //Toast.makeText(context, "correct link", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception?) {
                //Toast.makeText(context, "incorrect link", Toast.LENGTH_SHORT).show()
            }
        })
        //this.notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return listOfItems.get(position).type
    }

    override fun getViewTypeCount(): Int {
        return 7
    }

    private fun initText(view: EditText, detector: GestureDetectorCompat) {
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        param.setMargins(8, 8, 8, 8)

        view.textSize = 15.0f
        view.background = null
        view.layoutParams = param
        view.hint = "add something you,d like"

        view.setOnTouchListener { view, motionEvent ->
            detector.onTouchEvent(motionEvent)
        }
    }

    private fun initImage(view: ImageView, content: String) {
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(8, 8, 8, 8)

        val uri = Uri.parse(content)
        view.setImageURI(uri)
        view.setImageURI(uri)
        view.layoutParams = param
    }

    private fun initVideo(view: VideoView, mediaController: MediaController, content: String) {
        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(8, 8, 8, 8)

        val uri = Uri.parse(content)
        mediaController.setMediaPlayer(view)
        mediaController.setAnchorView(view)
        view.setMediaController(mediaController)
        view.setVideoPath(uri.path)

        view.setOnCompletionListener {
            view.setVideoURI(uri)
            view.start()
        }

        /*view.setVideoURI(uri)
        view.start()*/
    }

    private fun initLink(view: View, index: Int) {
        val close = view.findViewById<ImageButton>(R.id.link_close)
        val linkButton = view.findViewById<ImageButton>(R.id.link_button)

        linkButton.setOnClickListener {
            val text = view.findViewById<EditText>(R.id.link_url)
            listOfItems.removeAt(index)

            listOfItems.add(index, AddPostItem(5, text.text.toString()))
            this.notifyDataSetChanged()
        }

        close.setOnClickListener {
            //binding.postList.removeViewAt(index)
            listOfItems.removeAt(index)
            this.notifyDataSetChanged()
        }
    }
}


/*
override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val listItem = listOfItems.get(position)
        val type = getItemViewType(position)
        var view: View? = null

        if (convertView == null) {
            when (type) {
                ITEM_TEXT -> {
                    view = EditText(context)
                    view?.let { initText(it, listItem.textGestureDetector!!) }
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
                    view?.let { initLink(it, position) }
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
                    if(convertView is EditText) {
                        view = convertView
                    }
                    else {
                        view = EditText(context)
                        view?.let { initText(it, listItem.textGestureDetector!!) }
                    }
                }
                ITEM_IMAGE -> {
                    if(convertView is ImageView) {
                        initImage(convertView, listItem.content)
                        view = convertView
                    }
                    else {
                        view = ImageView(context)
                        view?.let { initImage(it, listItem.content) }
                    }
                }
                ITEM_VIDEO -> {
                    if(convertView is VideoView) {
                        initVideo(convertView, listItem.mediaController!!, listItem.content)
                        view = convertView
                    }
                    else {
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
                    if(!(convertView is EditText) && !(convertView is ImageView) && !(convertView is VideoView) && !(convertView is RichLinkView) && !(convertView is GPHMediaView)) {
                        view = convertView
                    }
                    else {
                        view =
                            LayoutInflater.from(context).inflate(R.layout.link_card, parent, false)
                        view?.let { initLink(it, position) }
                    }
                }

                ITEM_LINK_PREVIEW -> {
                    if(convertView is RichLinkView) {
                        initPreview(convertView, listItem.content)
                        view = convertView
                    }
                    else {
                        view = RichLinkView(context)
                        initPreview(view, listItem.content)
                    }
                }
                ITEM_GIPH -> {
                    if(convertView is GPHMediaView) {
                        initGiph(convertView, listItem.giphMedia)
                        view = convertView
                    }
                    else {
                        view = GPHMediaView(context)
                        initGiph(view, listItem.giphMedia)
                    }
                }
            }
            //view = convertView
        }


        return view!!
    }
 */