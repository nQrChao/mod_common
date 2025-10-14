package com.box.common.data.model

import java.io.Serializable

data class ModLiBaoListBean(
    var gameid: String = "",
    var gamename: String = "",
    var gameicon: String = "",
    var game_type: String = "",
    var genre_str: String = "",
    var cardlist: MutableList<ModLiBaoBean> = mutableListOf(),
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