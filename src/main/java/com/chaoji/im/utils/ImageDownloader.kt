package com.chaoji.im.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ImageDownloader {

    fun downloadImage(url: String): Bitmap? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            response.body?.byteStream()?.use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}