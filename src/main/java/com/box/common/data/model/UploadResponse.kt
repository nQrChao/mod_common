package com.box.common.data.model

import java.io.Serializable

/**
 * 上传接口返回的 data 字段的结构
 */
data class UploadResponse(
    var urls: MutableList<String> = mutableListOf(),
    var newFileNames: MutableList<String> = mutableListOf(),
    var fileNames: MutableList<String> = mutableListOf(),
    var originalFilenames: MutableList<String> = mutableListOf(),
) : Serializable {
}