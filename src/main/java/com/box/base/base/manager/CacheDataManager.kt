package com.box.base.base.manager

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal
object CacheDataManager {

    fun getTotalCacheSize(context: Context): String {
        var cacheSize: Long = getFolderSize(context.cacheDir)
        if ((Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.externalCacheDir!!)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if ((Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)) {
            deleteDir(context.externalCacheDir)
        }
    }


    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.isDirectory) {
            return dir.delete()
        }
        val children: Array<out String> = dir.list() ?: return false
        for (child: String in children) {
            deleteDir(File(dir, child))
        }
        return false
    }

    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val list: Array<out File> = file.listFiles() ?: return 0
            for (temp: File in list) {
                // 如果下面还有文件
                size += if (temp.isDirectory) {
                    getFolderSize(temp)
                } else {
                    temp.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }


    fun getFormatSize(size: Double): String {
        val kiloByte: Double = size / 1024
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K"
        }
        val megaByte: Double = kiloByte / 1024
        if (megaByte < 1) {
            return BigDecimal(kiloByte).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K"
        }
        val gigaByte: Double = megaByte / 1024
        if (gigaByte < 1) {
            return BigDecimal(megaByte).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M"
        }
        val teraBytes: Double = gigaByte / 1024
        if (teraBytes < 1) {
            return BigDecimal(gigaByte).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        return BigDecimal(teraBytes).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}