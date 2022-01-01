package com.example.android.tumblrx2.addpost.addpostfragments


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.R
import com.example.android.tumblrx2.databinding.FragmentInitialPostBinding
import com.example.android.tumblrx2.addpost.addpostfragments.postobjects.AddPostAdapter
import com.example.android.tumblrx2.addpost.addpostfragments.postobjects.AddPostItem
import com.example.android.tumblrx2.repository.AddPostRepository
import com.giphy.sdk.ui.Giphy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


/**
 * this is the starter fragment in the AddPostActivity
 * @property[binding] the fragment binding object the holds the UI
 * @property[oneVideo] a boolean that indicates that the post has one video
 * @property[adapter] the post list array adapter
 * @property[viewModel] the add post fragment view model that separates the logic from the UI
 * @property[oneVideo] a boolean to control that only one video is added
 * @property[blogList] the list that is used to save the Blogs info of the user
 * @property[userIds] list of user ids
 * @property[userHandles] list of user blogs handles
 * @property[userTitles] list of user blogs titles
 * @property[userImageUrls] list of user blogs images
 * @property[imageLauncher] used in register the result of image returned
 * @property[videoLauncher] used in register the result of video/gif returned
 */
class InitialPostFragment : Fragment() {


    private lateinit var binding: FragmentInitialPostBinding

    private var oneVideo: Boolean = false

    private val items = arrayOf("Image(s)", "Video/Gif")


    // the app sdk key for the giphy api
    private val appSdkKey = "7VZcIVNJ2mlp9FGeVAcFVY2a8NitNRVr"

    // the adapter
    private lateinit var adapter: AddPostAdapter

    // the AddPostViewModel
    private lateinit var viewModel: AddPostViewModel

    // user info
    var blogList = mutableListOf<BlogEntity>()
    var userIds = mutableListOf<String>()
    var userTitles = mutableListOf<String>()
    var userHandles = mutableListOf<String>()
    var userImageUrls = mutableListOf<String>()
    var currentBlog = 0

    // post type  1-published  2-private  3-draft
    var postType: String = "published"

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val clipData = it.data?.clipData
                // Use the uri to load the image
                if (clipData != null) {
                    Log.d("Initial fragment", "more than one image")
                    viewModel.addImages(clipData)
                } else {
                    Log.d("Initial fragment", "only one image")
                    it.data!!.data?.let { it1 -> viewModel.addImage(it1) }
                }
                adapter.notifyDataSetChanged()
            }
        }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data!!
                viewModel.addVideo(uri)
                oneVideo = true
                //adapter.notifyDataSetChanged()
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

        // observing the tags
        val tagObserver = Observer<Int> {
            insertTags(it)
        }
        viewModel.tagsCount.observe(viewLifecycleOwner, tagObserver)

        // observing the finish post Boolean
        val finishObserver = Observer<Boolean> {
            if(it)
                activity?.finish()
        }
        viewModel.finishPost.observe(viewLifecycleOwner, finishObserver)


        // init the list view and the adapter
        initPostList()



       // init the blog list
        initBlogList()



        // returning the root view
        return binding.root
    }


    /**
     * this function retrieves the user info from the server
     */

    fun initBlogList() {
        val repo = AddPostRepository()

        val call = repo.getBlogs(requireContext()).enqueue(object: retrofit2.Callback<MutableList<BlogEntity>>{
            override fun onResponse(
                call: Call<MutableList<BlogEntity>>,
                response: Response<MutableList<BlogEntity>>
            ) {
                if(response.isSuccessful) {
                    Log.d("initial fragment", "response happened with status ${response.code()}")
                    blogList = response.body()!!
                    Log.d("initial fragment", "blog list size is ${blogList.size} and first handle is")
                }
                initUserInfo()

                binding.profileName.setText(userHandles[0])
                if(userImageUrls.get(0) != null) {
                    Picasso.get().load(userImageUrls[0]).into(binding.profileImage)
                }
            }

            override fun onFailure(call: Call<MutableList<BlogEntity>>, t: Throwable) {
                Log.d("initial fragment", "response failed with message ${t.message}")
            }
        })



    }

    /**
     * this function initialize the user info such as his blogs handles, ids, titles and header images
     */
    fun initUserInfo() {
        for(blog in blogList) {
            Log.d("initial fragment","user handled: ${blog.handle}")
            userHandles.add(blog.handle)
            userIds.add(blog._id)
            userTitles.add(blog.title)
            userImageUrls.add(blog.headerImage)
        }
    }
    /**
     * this function observes the tags count from the view model to add them in the UI
     * @param[count] the current tags count if zero -> no tags are added
     */
    private fun insertTags(count: Int) {

        //binding.root.requestFocus()

        Log.d("initial fragment", "tags observed")

        if(count > 0) {
            binding.tagsCard.visibility = View.INVISIBLE

            binding.tagsArea.visibility = View.VISIBLE

            binding.tagsGroup.removeAllViews()

            for(tag in viewModel.selectedBlogs.value!!) {
                val chip = Chip(context)

                chip.text = tag

                binding.tagsGroup.addView(chip)
            }

        }
        else {
            binding.tagsCard.visibility = View.VISIBLE

            binding.tagsArea.visibility = View.INVISIBLE
        }

    }

    /**
     * this function initialise the post list
     */
    private fun initPostList() {
        Log.d("initial fragment", "init the adapter")
        adapter = context?.let { AddPostAdapter(it, 0, viewModel.postListItems.value!!, viewModel) }!!

        Log.d("initial fragment", "list items count = ${viewModel.postListItems.value!!.size}")

        binding.postList.adapter = adapter

        Log.d("initial fragment", "passed the error area")

        viewModel.adapter = adapter


    }


    /**
     * this function initialise all fragment views and sets their event listeners
     */

    private fun initialViews() {


        binding.insertLink.setOnClickListener {
            viewModel.insertLink()
            adapter.notifyDataSetChanged()
        }



        binding.close.setOnClickListener {
            activity?.finish()

        }

        binding.iconMore.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.more_bottomsheet, null, false)
            // creating a bottom sheet
            initMoresheet(view)
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()
        }

        binding.tags.setOnClickListener {

            val view = LayoutInflater.from(context).inflate(R.layout.tag_bottomsheet, null, false)
            viewModel.initTagSheet(view)

            // creating a bottom sheet
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()

        }

        binding.tagsCard.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.tag_bottomsheet, null, false)
            viewModel.initTagSheet(view)

            // creating a bottom sheet
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()
        }






        binding.insertMedia.setOnClickListener {

            checkForPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, "", 101)
        }

        binding.insertText.setOnClickListener {
            viewModel.insertOrEditText()
            //adapter.notifyDataSetChanged()
        }

        binding.insertText.setOnLongClickListener {
            // inflating the text bottom sheet layout view
            val view = LayoutInflater.from(context).inflate(R.layout.text_bottomsheet, null)

            // init the view buttons by the view model
            viewModel.initTextBottomSheet(view)

            // creating a bottom sheet with the view content
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()

            true
        }

        binding.insertGif.setOnClickListener {

            // inflating the giphy bottom sheet
            val view = LayoutInflater.from(context).inflate(R.layout.giphy_sheet, null)

            // init the giphy sheet
            viewModel.initialiseGifView(view)

            // creating a bottom sheet with grid of gifs
            val sheet = activity?.let { it1 -> BottomSheetDialog(it1) }
            sheet?.setContentView(view)
            sheet?.show()
        }


        binding.postButtonCard.setOnClickListener {
            viewModel.postToBlog(userIds[currentBlog], postType)
        }




        binding.profileCard.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Blogs")
                .setItems(userHandles.toTypedArray()) { dialog, which ->
                    // Respond to item chosen
                    binding.profileName.setText(userHandles.get(which))
                    if(userImageUrls.get(which) != null) {
                        Picasso.get().load(userImageUrls[which]).into(binding.profileImage)
                    }
                    currentBlog = which

                }
                .show()
        }



    }


    /**
     * this function initialize the views of the post options bottom sheet
     * @param[view] the inflated bottom sheet
     */
    fun initMoresheet(view: View) {
        val now = view.findViewById<MaterialCardView>(R.id.post_now_card)
        val draft = view.findViewById<MaterialCardView>(R.id.post_draft_card)
        val private = view.findViewById<MaterialCardView>(R.id.post_private_card)

        val buttonNow = view.findViewById<RadioButton>(R.id.now_button)
        val buttonDraft = view.findViewById<RadioButton>(R.id.draft_button)
        val buttonPrivate = view.findViewById<RadioButton>(R.id.private_button)

        when(postType) {
            "published" -> buttonNow.isChecked = true
            "draft" -> buttonDraft.isChecked = true
            "private" -> buttonPrivate.isChecked = true
        }

        now.setOnClickListener {
            buttonDraft.isChecked = false
            buttonPrivate.isChecked = false
            buttonNow.isChecked = true
            postType = "published"
            binding.postButtonText.setText("Post")
        }
        draft.setOnClickListener {
            buttonNow.isChecked = false
            buttonPrivate.isChecked = false
            buttonDraft.isChecked = true
            postType = "draft"
            binding.postButtonText.setText("Save draft")
        }
        private.setOnClickListener {
            buttonNow.isChecked = false
            buttonDraft.isChecked = false
            buttonPrivate.isChecked = true
            postType = "private"
            binding.postButtonText.setText("Private")
        }

        buttonNow.setOnClickListener {
            buttonNow.isChecked = true
            buttonDraft.isChecked = false
            buttonPrivate.isChecked = false
            postType = "published"
            binding.postButtonText.setText("Post")
        }
        buttonDraft.setOnClickListener {
            buttonNow.isChecked = false
            buttonPrivate.isChecked = false
            buttonDraft.isChecked = true
            postType = "draft"
            binding.postButtonText.setText("Save draft")
        }
        buttonPrivate.setOnClickListener {
            buttonNow.isChecked = false
            buttonDraft.isChecked = false
            buttonPrivate.isChecked = true
            postType = "private"
            binding.postButtonText.setText("Private")
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
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imageLauncher.launch(intent)

    }

    /**
     * inserts a video under the editor cursor position
     */

    private fun insertVideo() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }

    /**
     * this function checks for the specified permission
     * @param[permission] the permission to ask the user to grant
     * @param[code] the permission request code
     */

    fun checkForPermission(permission: String, name: String, code: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                    // add the code here
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
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, code)

                else -> requireActivity().requestPermissions(arrayOf(permission), code)
            }
        }
    }

    /**
     * this function shows a dialog to request a permission
     * @param[permission] the permission to request
     * @param[code] the requesting code
     */

    fun showDialog(permission: String, code:Int ) {
        val builder = AlertDialog.Builder(context)

        builder.apply {
            setMessage("permission to access the media is required")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                requireActivity().requestPermissions(arrayOf(permission), code)
            }
        }

        val dialog = builder.create()

        dialog.show()
    }


}
