package com.box.common.utils

import android.content.Context
import java.io.File
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

/**
 * 缓存管理器
 */
object CacheManager {

    /**
     * 获取格式化后的总缓存大小 (例如 10.5 MB)
     * @param context Context
     * @return 格式化后的字符串
     */
    fun getFormattedTotalCacheSize(context: Context): String {
        // 1. 调用现有的 getTotalCacheSize 获取以 bytes 为单位的总大小
        val totalBytes = getTotalCacheSize(context)
        // 2. 调用现有的 formatSize 将 bytes 转换为 "KB", "MB", "GB" 等
        return formatSize(totalBytes)
    }


    /**
     * 获取总缓存大小（内部 + 外部）
     * @return 缓存大小，单位 B
     */
    fun getTotalCacheSize(context: Context): Long {
        var cacheSize = getDirSize(context.cacheDir)
        if (context.externalCacheDir != null && context.externalCacheDir!!.exists()) {
            cacheSize += getDirSize(context.externalCacheDir!!)
        }
        return cacheSize
    }

    /**
     * 清除所有缓存（内部 + 外部）
     */
    fun clearAllCache(context: Context) {
        clearDirContents(context.cacheDir)
        if (context.externalCacheDir != null) {
            clearDirContents(context.externalCacheDir!!)
        }
    }

    /**
     * 清理目录内容，但不删除目录本身
     */
    private fun clearDirContents(dir: File?): Boolean {
        if (dir == null || !dir.exists() || !dir.isDirectory) {
            return false
        }

        // listFiles() 可能返回 null
        dir.listFiles()?.forEach { file ->
            if (file.isFile) {
                file.delete() // 删除文件
            } else if (file.isDirectory) {
                clearDirContents(file) // 递归删除子目录内容
                file.delete() // 删除空的子目录
            }
        }
        return true
    }

    /**
     * 递归计算目录大小
     */
    private fun getDirSize(dir: File?): Long {
        if (dir == null || !dir.exists() || !dir.isDirectory) {
            return 0L
        }

        var size = 0L
        // listFiles() 可能返回 null
        dir.listFiles()?.forEach { file ->
            size += if (file.isFile) {
                file.length()
            } else {
                getDirSize(file) // 递归
            }
        }
        return size
    }

    /**
     * 格式化文件大小 (例如 10.5 MB)
     */
    fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "Kb", "Mb", "Gb", "Tb")
        val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()
        return String.format(
            Locale.US, "%.2f %s",
            bytes / 1024.0.pow(digitGroups.toDouble()),
            units[digitGroups]
        )
    }


}