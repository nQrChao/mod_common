package com.chaoji.im.data.model

import java.io.Serializable

data class ModMeYouHuiQuanBean(
    val expiry_soon: String = "",
    val xh_username: String = "",
    val used_time: String = "",
    val gameid: String = "",
    val game_type: String = "",
    val amount: String = "",
    val use_cdt: String = "",
    val expiry: String = "",
    val badge: String = "",
    val sign: String = "",
    val used_game_name: String = "",
    val query_info: String = "",
    val coupon_name: String = "",
    val range: String = "",
    val gamename: String = "",
    val gameicon: String = "",
    val genre_str: String = "",
    val game_labels: MutableList<ModGameLabelBean> = mutableListOf(),
) :Serializable {



}

