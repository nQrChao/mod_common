package com.box.common.utils

import com.box.common.AppInit
import java.io.File

/**
 * - 使用 object 关键字实现安全的单例模式。
 * - 移除了可变的静态变量 APK_FILE_PATH，所有路径都通过函数动态生成，更安全、更可靠。
 */
object DirUtils {
    // --- 核心目录常量 ---
    private const val LOG_DIR_NAME = "log"
    private const val GAME_DIR_NAME = "game"
    private const val DOWNLOAD_DIR_NAME = "downloadFile"
    private const val PUBLIC_RES_DIR_NAME = "publicRes"
    private const val SCREEN_SHOT_DIR_NAME = "screenShot"
    const val SCREEN_RECORDING_DIR_NAME = "screenRecording"

    /**
     *  私有的辅助函数，用于在返回路径前确保目录存在
     * @param path 目录的完整路径
     * @return 传入的路径字符串
     */
    private fun ensureDirExists(path: String): String {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        return path
    }

    /**
     * 获取应用内部文件存储的根目录 (e.g., /data/data/com.your.package/files/)
     * 这是所有自定义目录的基础
     */
    private val appFilesRoot: String by lazy { AppInit.application.filesDir.absolutePath + File.separator }


    /**
     * 获取游戏目录
     * e.g., /data/data/com.your.package/files/game/
     */
    fun getGameFileDir(): String = ensureDirExists(appFilesRoot + GAME_DIR_NAME + File.separator)

    /**
     * 获取日志文件存储目录
     */
    fun getLogDir(): String = ensureDirExists(getGameFileDir() + LOG_DIR_NAME + File.separator)

    /**
     * 获取下载文件的目录
     */
    fun getDownFileDir(): String = ensureDirExists(getGameFileDir() + DOWNLOAD_DIR_NAME + File.separator)

    /**
     * 获取截图目录
     */
    fun getScreenShotDir(): String = ensureDirExists(getGameFileDir() + SCREEN_SHOT_DIR_NAME + File.separator)

    /**
     * 获取录像目录
     */
    fun getScreenRecordingDir(): String = ensureDirExists(appFilesRoot + SCREEN_RECORDING_DIR_NAME + File.separator)

    /**
     * 获取公共缓存资源目录
     */
    fun getPublicResDir(): String = ensureDirExists(getGameFileDir() + PUBLIC_RES_DIR_NAME + File.separator)

    /**
     * 获取私有缓存资源目录
     */
    fun getOutFilePath(gameId: String): String {
        val path = getGameFileDir() + gameId + File.separator + gameId + File.separator
        return ensureDirExists(path)
    }

    /**
     * 获取游戏 zip 包的完整路径
     * (注意：这个是文件路径，不是目录，所以不需要创建)
     */
    fun getGameZipFilePath(gameId: String): String {
        return getGameFileDir() + "$gameId.zip"
    }

    /**
     * 获取 zip 包的解压目录
     */
    fun getZipOutPath(gameId: String): String {
        // 这个方法实际是获取一个目录，所以也需要确保它存在
        return getOutFilePath(gameId)
    }

    /**
     * MimeType
     */
    fun getMimeType(name: String): String {
        return when (name.lowercase()) {
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
            else -> "file/*"
        }
    }
}