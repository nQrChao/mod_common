package com.box.common.data.model

import java.io.Serializable

/**
 * 上传接口返回的 data 字段的结构
 */
data class UploadResponseString(
    var msg: String = "",
    var code: Int = 0,
    var urls: String = "",
    var imgUrl: String = "",
    var newFileNames: String = "",
    var fileNames: String = "",
    var originalFilenames: String = "",
) : Serializable {
}