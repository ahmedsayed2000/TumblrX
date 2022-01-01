package com.example.android.tumblrx2.addpost.addpostfragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.android.tumblrx2.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.currentCoroutineContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.manipulation.Ordering
import org.mockito.Mockito

class AddPostViewModelTest {

    private lateinit var viewModel: AddPostViewModel

    @Before
    fun setUp() {
        viewModel = AddPostViewModel()
    }

    @Test
    fun `test first editor added` () {
        val value = viewModel.postListItems.getOrAwaitValue()
        val size = value.size
        assertThat(size).isGreaterThan(0)
    }

    @Test
    fun `toggle post button`() {
        var value = viewModel.togglePostButton.getOrAwaitValue()
        value = true

        assertThat(value).isTrue()

    }
}