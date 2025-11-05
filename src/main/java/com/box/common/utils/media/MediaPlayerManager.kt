package com.box.common.utils.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.box.other.blankj.utilcode.util.Logs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * 音频播放管理类
 */
object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null  //播放音频API类：MediaPlayer
    private var isPause = false  //是否暂停
    val isPlaying = mediaPlayer?.let {//是否正在播放
        it.isPlaying
    }

    /**
     * 播放声音
     *
     */
    fun playSound(context: Context, filePath: String, completeListener: MediaPlayer.OnCompletionListener) {
        if (mediaPlayer == null) {
            //初始化mediaPlayer
            mediaPlayer = MediaPlayer()
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            mediaPlayer?.setAudioAttributes(audioAttributes)
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                mediaPlayer?.reset()
                false
            }
        } else {
            mediaPlayer?.reset()
        }

        runBlocking {
            launch {
                mediaPlayer?.apply {
                    setDataSource(context, Uri.parse(filePath))
                    setOnCompletionListener(completeListener)
                    setOnPreparedListener {
                        start()
                    }
                    prepareAsync()

                }


            }

        }

    }

    /**
     * 暂停播放
     */
    fun pause() {
        mediaPlayer?.takeIf {
            it.isPlaying
        }?.let {
            it.pause()
            isPause = true
        }
    }

    /**
     * 恢复播放
     */
    fun resume() {
        mediaPlayer?.takeIf {
            isPause
        }?.let {
            it.start()
            isPause = false
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        mediaPlayer?.takeIf {
            it.isPlaying
        }?.let {
            it.stop()
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }


    suspend fun hasAudioMimeType(url: String): Boolean = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val fileUrl = URL(url)
            connection = fileUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            val contentType = connection.contentType
            Logs.e("contentType：" + contentType)
            contentType != null && contentType.startsWith("audio/")
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            connection?.disconnect()
        }
    }
}