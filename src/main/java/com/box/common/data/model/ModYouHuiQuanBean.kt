package com.box.common.data.model

import java.io.Serializable

data class ModYouHuiQuanBean(
    var id: String = "",
    var gameid: String = "",
    var pft: String = "",
    var cdt: String = "",
    var gamename: String = "",
    var gameshort: String = "",
    var gameicon: String = "",
    var game_type: String = "",
    var payrate: String = "",
    var gamecoin: String = "",
    var genre_ids: String = "",
    var client_type: String = "",
    var sort_newest: String = "",
    var sort_hot: String = "",
    var sort_ranking: String = "",
    var sort_select: String = "",
    var game_summary: String = "",
    var game_description: String = "",
    var screenshot1: String = "",
    var screenshot2: String = "",
    var screenshot3: String = "",
    var screenshot4: String = "",
    var screenshot5: String = "",
    var is_first: String = "",
    var whole_enable: String = "",
    var online_time: String = "",
    var offline_time: String = "",
    var vid: String = "",
    var hide_discount_label: String = "",
    var show_discount_total: String = "",
    var status: String = "",
    var video_pic: String = "",
    var video_url: String = "",
    var bg_pic: String = "",
    var is_refund: String = "",
    var is_reserve: String = "",
    var mtr_url: String = "",
    var customize: String = "",
    var game_mode: String = "",
    var data_exchange: String = "",
    var selected_game: String = "",
    var record_number: String = "",
    var accelerate_status: String = "",
    var cps_forbidden: String = "",
    var has_get: String = "",
    var isSelect :Boolean = false
) : Serializable {
    val displayName: String
        get() {
            // 先判断是否为 null
            if (gamename == null) return ""
            // 找到第一个'（'的位置
            val index = gamename.indexOf('（')
            // 如果找到了，就截取前面的部分；否则返回原字符串
            return if (index != -1) gamename.substring(0, index) else gamename
        }
    val pftAsInt: Int
        get() = pft.toDoubleOrNull()?.toInt() ?: 0

    val bgAlpha: Float
        get() = if (has_get == "yes") 0.5f else 1.0f
    val itemHasGet: Boolean
        get() = has_get == "yes"

    val itemHasGetText: String
        get() = if (has_get == "yes") "已领取" else "立即领取"
    val itemHasGetText2: String
        get() = if (has_get == "yes") "已领取" else "领取"

}