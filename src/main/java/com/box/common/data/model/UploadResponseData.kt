package com.box.common.data.model

import com.google.gson.annotations.SerializedName

/**
 * 上传接口返回的 data 字段的结构
 */
data class UploadResponseData(
    // 假设 urls 字段对应的是一个 URL 字符串列表
    @SerializedName("urls")
    val urls: List<String>?,

    @SerializedName("newFileNames")
    val newFileNames: String?,

    @SerializedName("fileNames")
    val fileNames: String?,

    @SerializedName("originalFilenames")
    val originalFilenames: String?
)