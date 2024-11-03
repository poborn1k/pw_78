package com.example.pw_78

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.net.URL

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class MainActivityTest {
    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        activity = Robolectric.buildActivity(MainActivity::class.java, intent)
            .create()
            .start()
            .resume()
            .visible()
            .get()
    }

    @org.junit.Test
    fun loadImage_validURL() {
        val validImageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
        val actualBitmap = activity.createBitmapFromURL(validImageUrl)
        Assert.assertNotNull(actualBitmap)
    }

    @org.junit.Test
    fun loadImage_invalidURL() {
        val validImageUrl = "fdsfdsfdsfds"
        val actualBitmap = activity.createBitmapFromURL(validImageUrl)
        Assert.assertNull(actualBitmap)
    }

    @org.junit.Test
    fun loadImage_emptyURL() {
        val validImageUrl = ""
        val actualBitmap = activity.createBitmapFromURL(validImageUrl)
        Assert.assertNull(actualBitmap)
    }

    @org.junit.Test
    fun saveImage_correct() {
        val validImageUrl = URL("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png")
        val actualBitmap = BitmapFactory.decodeStream(validImageUrl.openStream())
        activity.saveImage(actualBitmap)
        assert(File(activity.filesDir, "new_image.jpg").exists())
    }

    @org.junit.Test
    fun saveImage_wrong() {
        val invalidImageUrl = URL("https://shorturl.at/4wb8tregfdsE")
        val actualBitmap = BitmapFactory.decodeStream(invalidImageUrl.openStream())
        activity.saveImage(actualBitmap)
        assert(File(activity.filesDir, "new_image.jpg").exists())
    }
}