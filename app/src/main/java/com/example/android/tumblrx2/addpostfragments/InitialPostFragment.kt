package com.example.android.tumblrx2.addpostfragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.AddPostViewModel
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.databinding.FragmentInitialPostBinding
import com.example.android.tumblrx2.postobjects.AddPostAdapter
import com.example.android.tumblrx2.postobjects.AddPostItem
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GiphyGridView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * this is the starter fragment in the AddPostActivity
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


    private lateinit var binding: FragmentInitialPostBinding

    private lateinit var mediaController: MediaController
    private var oneVideo: Boolean = false

    private val items = arrayOf("Image(s)", "Video/Gif")

    private val textSizes = arrayOf("Regular", "Bigger", "Biggest")
    private var textMap = mutableMapOf<Int, Int>()


    // the app sdk key for the giphy api
    private val appSdkKey = "7VZcIVNJ2mlp9FGeVAcFVY2a8NitNRVr"

    // the adapter and its list
    var listOfItems: MutableList<AddPostItem> = mutableListOf()
    private lateinit var adapter: AddPostAdapter

    // the AddPostViewModel
    private lateinit var viewModel: AddPostViewModel


    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val clipData = it.data?.clipData
                // Use the uri to load the image
                if (clipData != null) {
                    Log.d("Initial fragment", "more than one image")
                    viewModel.addImages(clipData)
                } else {
                    it.data!!.data?.let { it1 -> viewModel.addImage(it1) }
                }
                adapter.notifyDataSetChanged()
            }
        }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data!!


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


        // init all the bottom sheets and buttons listeners
        initialViews()

        // init the video media controller
        mediaController = MediaController(context)

        // giphy configuration
        context?.let { Giphy.configure(it, appSdkKey) }

        // initialising the view model
        Log.d("initial fragment", " view model about to be created")
        viewModel = ViewModelProvider(this).get(AddPostViewModel::class.java)
        context?.let { viewModel.initViews(binding.postList, it) }


        // observing the post list items changing
        val listObserver = Observer<MutableList<AddPostItem>> {
            adapter.notifyDataSetChanged()
        }
        viewModel.postListItems.observe(viewLifecycleOwner, listObserver)

        // observing the post button
        val togglePostButtonObserver = Observer<Boolean> {
            togglePostButton(it)
        }
        viewModel.togglePostButton.observe(viewLifecycleOwner, togglePostButtonObserver)


        initPostList()

        //initialViews()

        // for link previewer
        /*richPreview = RichPreview(object : ResponseListener {
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
        })*/


        // returning the root view
        return binding.root
    }

    /**
     * this function initialise the post list
     */
    private fun initPostList() {
        Log.d("initial fragment", "init the adapter")
        adapter = context?.let { AddPostAdapter(it, 0, viewModel.postListItems.value!!) }!!

        Log.d("initial fragment", "list items count = ${viewModel.postListItems.value!!.size}")

        binding.postList.adapter = adapter

        Log.d("initial fragment", "passed the error area")


    }


    /**
     * this function initialise all fragment views and sets their event listeners
     */

    private fun initialViews() {


        /*textMap[0] = 0
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
        })*/

        binding.insertLink.setOnClickListener {
            viewModel.insertLink()
            adapter.notifyDataSetChanged()
        }



        binding.close.setOnClickListener {
            activity?.finish()
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
            viewModel.insertOrEditText()
            adapter.notifyDataSetChanged()
        }

        binding.insertText.setOnLongClickListener {
            // inflating the text bottom sheet layout view
            val view = LayoutInflater.from(context).inflate(R.layout.text_bottomsheet, null)

            // init the view buttons
            viewModel.initTextBottomSheet(view)

            // creating a bottom sheet with the view content
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
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
                /*val mediaView = context?.let { GPHMediaView(it) }
                mediaView?.setMedia(media, RenditionType.original)
                val param = RelativeLayout.LayoutParams(
                    100,
                    100
                )
                param.setMargins(8, 8, 10, 20)*/
                val item = AddPostItem(6, "")
                item.giphMedia = media
                //val index = getActiveText()
                val index = 0
                if (index == -1) {
                    listOfItems.add(item)
                    Log.d("initial fragment", "last view")
                } else {
                    listOfItems.add(index + 1, item)
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
     * this function adds a text editor when clicking on an empty space in the post or on the insert Text Button
     */


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


}
