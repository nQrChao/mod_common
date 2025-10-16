package com.box.common.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.box.other.blankj.utilcode.util.AppUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * 将媒体文件保存到系统相册的工具类
 */
object MediaSaveUtils {

    private const val TAG = "MediaSaveUtils"

    /**
     * 将视频文件保存到系统相册 (兼容新旧安卓版本)
     */
    fun saveVideoToAlbum(context: Context, videoFile: File): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            saveVideoToAlbumBeforeQ(context, videoFile)
        } else {
            saveVideoToAlbumAfterQ(context, videoFile)
        }
    }

    /**
     * Android Q (API 29) 以下版本的保存逻辑
     */
    private fun saveVideoToAlbumBeforeQ(context: Context, videoFile: File): Boolean {
        // DCIM/YourAppName/video.mp4
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val appDir = File(dcimDir, AppUtils.getAppName())
        if (!appDir.exists() && !appDir.mkdirs()) {
            Log.e(TAG, "Failed to create app directory in DCIM.")
            return false
        }
        val destFile = File(appDir, videoFile.name)

        return try {
            FileInputStream(videoFile).use { ins ->
                BufferedOutputStream(FileOutputStream(destFile)).use { ous ->
                    ins.copyTo(ous)
                }
            }
            // 通知系统媒体库扫描新文件
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destFile.absolutePath),
                arrayOf("video/mp4")
            ) { path, uri ->
                Log.i(TAG, "Scan completed for: $path, uri: $uri")
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Android Q (API 29) 及以上版本的保存逻辑 (使用 MediaStore API)
     */
    private fun saveVideoToAlbumAfterQ(context: Context, videoFile: File): Boolean {
        val values = ContentValues().apply {
            put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + File.separator + AppUtils.getAppName())
            put(MediaStore.Video.Media.TITLE, videoFile.name)
            put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.name)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Video.Media.IS_PENDING, 1) // 先标记为待处理
        }

        val resolver = context.contentResolver
        val collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri: Uri? = resolver.insert(collection, values)

        if (uri == null) {
            Log.e(TAG, "Failed to create new MediaStore record.")
            return false
        }

        try {
            // 写入文件数据
            resolver.openFileDescriptor(uri, "w", null).use { pfd ->
                requireNotNull(pfd) { "ParcelFileDescriptor is null" }
                FileOutputStream(pfd.fileDescriptor).use { out ->
                    FileInputStream(videoFile).use { ins ->
                        ins.copyTo(out)
                    }
                }
            }
            // 更新状态为非待处理
            values.clear()
            values.put(MediaStore.Video.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果出错，删除不完整的记录
            resolver.delete(uri, null, null)
            return false
        }
    }
}