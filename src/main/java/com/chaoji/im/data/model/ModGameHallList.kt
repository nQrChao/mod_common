package com.chaoji.im.data.model

import java.io.Serializable
import kotlin.random.Random

data class ModGameHallList(
    val gameid: String = "",
    val gamename: String = "",
    val gameicon: String = "",
    val game_type: String = "",
    val game_summary: String = "",
    val is_first: String = "",
    val online_time: String = "",
    val hide_discount_label: String = "",
    val bg_pic: String = "",
    val selected_game: String = "",
    val accelerate_status: String = "",
    val genre_str: String = "",
    val coupon_amount: String = "",
    val card_count: String = "",
    val play_count: String = "",
    val server_str: String = "",
    val next_server_time: String = "",
    val discount: String = "",
    val built_in_discount: String = "",
    val free: String = "",
    val unshare: String = "",
    val is_reserve_status: String = "",
    val game_labels: MutableList<ModGameLabelBean> = mutableListOf(),

    ) : Serializable {

    // 添加一个新的计算属性
    val displayName: String
        get() {
            // 先判断是否为 null
            if (gamename == null) return ""
            // 找到第一个'（'的位置
            val index = gamename.indexOf('（')
            // 如果找到了，就截取前面的部分；否则返回原字符串
            return if (index != -1) gamename.substring(0, index) else gamename
        }

    /**
     * 获取 1 到 50 之间的随机数，保留小数点后1位
     */
    fun getRandomPlayerCount(): Double {
        // 生成 10 到 500 之间的随机整数
        val randomInt = Random.nextInt(10, 501)
        // 将整数除以 10.0 得到一位小数的浮点数
        return randomInt / 10.0
    }

}

