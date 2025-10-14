package com.chaoji.im.data.model

import java.io.Serializable

data class ModAppStyle(
    var app_header_info: HeaderInfo? = null,
    val interstitial: ModAppJumpInfoBean? = null,
    var app_bottom_info: BottomInfo? = null,
    val game_button_color: SimpleInfo? = null,
    val container_icon: SimpleInfo? = null
) : Serializable {
    data class HeaderInfo(
        var default_color: String? = null,
        var header_bg: String? = null,
        var selected_color: String? = null,
        var header_bg_height: String? = null,
        var header_bg_width: String? = null,
        val url: String? = null
    )

    data class BottomInfo(
        var index_button_default_icon: String? = null,
        var index_button_selected_icon: String? = null,
        var server_button_default_icon: String? = null,
        var server_button_selected_icon: String? = null,
        var type_button_default_icon: String? = null,
        var type_button_selected_icon: String? = null,
        var service_button_default_icon: String? = null,
        var service_button_selected_icon: String? = null,
        var button_default_color: String? = null,
        var button_selected_color: String? = null,
        var center_button_icon: String? = null
    )


    data class SimpleInfo(
        var btn_color: String? = null,
        var container_icon: String? = null
    )
}