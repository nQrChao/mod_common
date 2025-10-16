package com.box.common.utils

/**
 * 处理 URL 的工具类
 */
object UrlUtils {

    /**
     * 从 URL 中提取路径部分 (移自 UtilDir.getPath)
     */
    fun getPathFromUrl(url: String?): String {
        if (url.isNullOrEmpty()) return ""
        return try {
            val fileName = url.substring(url.lastIndexOf("/") + 1)
            val p1 = url.removePrefix("http://").removePrefix("https://")
            val parts = p1.split("/")
            if (parts.size <= 1) return p1 // e.g., "www.example.com"

            val pathWithoutHost = p1.substring(parts[0].length)
            val finalPath = pathWithoutHost.removeSuffix(fileName)
            if (finalPath.length < 2) p1 else finalPath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 从 URL 中获取域名 (移自 UtilDir.getDomain)
     */
    fun getDomainFromUrl(url: String?): String {
        if (url.isNullOrEmpty()) return ""
        return try {
            url.removePrefix("http://").removePrefix("https://").split("/").firstOrNull() ?: url
        } catch (e: Exception) {
            e.printStackTrace()
            url
        }
    }

    /**
     * 从 file:// 路径中剥离协议头 (移自 UtilDir.getDomainFile)
     */
    fun getPathFromFileUrl(fileUrl: String?): String {
        if (fileUrl.isNullOrEmpty()) return ""
        return try {
            fileUrl.removePrefix("file://").removePrefix("file:/")
        } catch (e: Exception) {
            e.printStackTrace()
            fileUrl
        }
    }
}