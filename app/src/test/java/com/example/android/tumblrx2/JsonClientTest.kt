package com.example.android.tumblrx2

import com.example.android.tumblrx2.postobjects.ImageObject
import com.example.android.tumblrx2.postobjects.PostContent
import com.example.android.tumblrx2.postobjects.PostObject
import com.example.android.tumblrx2.postobjects.TextObject
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * this class to test the parsing service of the post content to Json array
 */

class JsonClientTest {

    val list1 = ArrayList<PostObject>()
    val list2 = ArrayList<PostObject>()

    val parseService = Mockito.mock(ParseJsonService::class.java)

    lateinit var client: JsonClient


    /**
     * this function tests parsing a text object only
     */
    @Test
    fun `test json parsing text`() {
        Mockito.`when`(parseService.parse(list1)).thenReturn(PostContent(list1))
        val jsonString = client.getJson(list1)
        assertNotNull(jsonString)
    }

    /**
     * this function tests parsing an image object only
     */

    @Test
    fun `test json parsing image`() {
        Mockito.`when`(parseService.parse(list2)).thenReturn(PostContent(list2))
        val jsonString = client.getJson(list2)
        assertNotNull(jsonString)
    }


    /**
     * used for initialising
     */
    @Before
    fun setUp() {
        client = JsonClient(parseService)
        val text = TextObject()
        val image = ImageObject()
        image.width = 20
        image.height = 20
        image.file = null
        text.text = "Hello world"
        list1.add(text)
        list2.add(image)
    }
}