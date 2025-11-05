package com.box.common.utils

import com.box.common.AppInit
import java.io.File

/**
 * 1. 使用 object 关键字实现安全的单例模式。
 * 2. 公共函数统一返回 File 对象，而不是 String，方便调用者使用。
 * 3. 路径拼接使用 .resolve() 扩展函数，更安全、更符合 Kotlin 习惯。
 * 4. 区分 filesDir (永久数据) 和 cacheDir (临时数据)。
 */
object DirUtils {
    // --- 核心目录常量 ---
    private const val DIR_LOG = "log"
    private const val DIR_GAME = "game"
    private const val DIR_DOWNLOAD = "downloadFile"
    private const val DIR_PUBLIC = "public"
    private const val DIR_SCREEN = "screen"
    const val DIR_SCREEN_RECORDING = "screenRecording" // 假设这个是永久保存的

    /**
     * 私有的辅助函数，用于在返回 File 对象前确保目录存在
     * @param dirFile 目标目录的 File 对象
     * @return 传入的 File 对象 (此时已确保 mkdirs() 已执行)
     */
    private fun ensureDirExists(dirFile: File): File {
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        return dirFile
    }

    // --- 根目录 ---

    /**
     * 获取应用内部文件存储的根目录 (e.g., /data/data/com.your.package/files)
     * 用于存放永久性数据。
     */
    private val appFilesRoot: File by lazy { AppInit.application.filesDir }

    /**
     * 获取应用内部缓存的根目录 (e.g., /data/data/com.your.package/cache)
     * 用于存放临时数据，可能被系统清理。
     */
    private val appCacheRoot: File by lazy { AppInit.application.cacheDir }


    // --- 永久目录 (基于 filesDir) ---
    /**
     * 获取游戏主目录 (永久)
     * e.g., /data/data/com.your.package/files/game
     */
    fun getGameFileDir(): File = ensureDirExists(appFilesRoot.resolve(DIR_GAME))

    /**
     * 获取截图目录 (永久)
     * e.g., /data/data/com.your.package/files/game/screenShot
     */
    fun getScreenShotDir(): File = ensureDirExists(getGameFileDir().resolve(DIR_SCREEN))

    /**
     * 获取录像目录 (永久)
     * e.g., /data/data/com.your.package/files/screenRecording
     */
    fun getScreenRecordingDir(): File = ensureDirExists(appFilesRoot.resolve(DIR_SCREEN_RECORDING))

    /**
     * 获取某个游戏专属的私有目录 (永久)
     * (已优化掉 gameId/gameId 嵌套)
     * e.g., /data/data/com.your.package/files/game/GAME_ID_123
     */
    fun getGameSpecificDir(gameId: String): File {
        return ensureDirExists(getGameFileDir().resolve(gameId))
    }

    /**
     * 获取游戏 zip 包的**完整文件路径** (非目录)
     * (注意：这个是文件路径，不是目录，所以不需要创建)
     * e.g., /data/data/com.your.package/files/game/GAME_ID_123.zip
     */
    fun getGameZipFile(gameId: String): File {
        return getGameFileDir().resolve("$gameId.zip")
    }

    /**
     * 获取 zip 包的解压目录
     */
    fun getZipOutPath(gameId: String): File {
        // 复用 getGameSpecificDir
        return getGameSpecificDir(gameId)
    }

    // --- 缓存目录 (基于 cacheDir) ---

    /**
     * 获取日志文件存储目录 (临时)
     * e.g., /data/data/com.your.package/cache/log
     */
    fun getLogDir(): File = ensureDirExists(appCacheRoot.resolve(DIR_LOG))

    /**
     * 获取下载文件的目录 (临时)
     * e.g., /data/data/com.your.package/cache/downloadFile
     */
    fun getDownFileDir(): File = ensureDirExists(appCacheRoot.resolve(DIR_DOWNLOAD))

    /**
     * 获取公共缓存资源目录 (临时)
     * e.g., /data/data/com.your.package/cache/publicRes
     */
    fun getPublicResDir(): File = ensureDirExists(appCacheRoot.resolve(DIR_PUBLIC))

    // --- 其他工具 ---
    /**
     * MimeType
     * (这个函数与目录无关，未来可以考虑移到 FileUtils.kt 中)
     */
    fun getMimeType(name: String): String {
        return when (name.lowercase().substringAfterLast('.', name.lowercase())) {
            "jpeg", "jpg" -> "image/jpeg"
            "gif" -> "image/gif"
            "png" -> "image/png"
            "ico" -> "image/x-icon"
            "mp3" -> "audio/mpeg"
            "css" -> "text/css"
            "htm", "html" -> "text/html"
            "txt", "atlas" -> "text/plain"
            "bin", "json", "_json", "fnt", "sk" -> "application/octet-stream"
            "js" -> "application/x-javascript"
            else -> "application/octet-stream" // 默认改为 application/octet-stream 比 file/* 更常用
        }
    }
}