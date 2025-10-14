package com.box.common.data.model

import java.io.Serializable

data class ModVideoParam(
    var video_url: String? = null,
    var video_title: String? = null,
    var video_description: String? = null,
    var video_preview: String? = null,
    var video_width: Int = 0,
    var video_height: Int = 0
) : Serializable {
}