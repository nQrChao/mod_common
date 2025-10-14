package com.chaoji.im.data.model

import java.io.Serializable

class AppletsXiaoGameBean(
    val appId: String = "",
    val pic: String = "",
    val gameName: String = "",
    val gamename: String = "",
    val gameIcon: String = "",
    val genre_ids: String = "",
    val bg_pic: String = "",
    val xgameid: String = "",
    val genre_str: String = "",
    val reward: String = "",
    val status: String = "",
    val gameid: String = "",
    val btnName: String = "",
    val app_title: String = "",
    val app_title2: String = "",
    val app_title3: String = "",
    val app_title4: String = "",
    var gameUrl: String = "",
    var select: Boolean = false,
    var first: Boolean = false,
    var xdfirst: Boolean = false,

    ) : Serializable {
    fun shouldShowView(): Boolean {
        return !first && xdfirst
    }
}