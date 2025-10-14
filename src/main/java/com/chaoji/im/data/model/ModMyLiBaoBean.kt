package com.chaoji.im.data.model

import java.io.Serializable

data class ModMyLiBaoBean(
    var id: String = "",
    var uid: String = "",
    var cardid: String = "",
    var gameid: String = "",
    var card: String = "",
    var cardname: String = "",
    var gettime: String = "",
    var xh_username: String = "",
    var gettime2: String = "",
    var gamename: String = "",
    var gameicon: String = "",
    var game_type: String = "",
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

}