package com.box.common.data.model

import com.box.other.blankj.utilcode.util.StringUtils
import java.io.Serializable

data class ModLiBaoBean(
    var cardid: String = "",
    var gameid: String = "",
    var cardname: String = "",
    var begintime: String = "",
    var endtime: String = "",
    var sort: String = "",
    var cardusage: String = "",
    var cardcontent: String = "",
    var cardcountall: String = "",
    var cardkucun: String = "",
    var need_pay_total: String = "",
    var card_type: String = "",
    var need_pay_type: String = "",
    var need_pay_begin: String = "",
    var need_pay_end: String = "",
    var sign: String = "",
    var common_card: String = "",
    var gamename: String = "",
    var gameicon: String = "",
    var game_type: String = "",
    var youxiaoqi: String = "",
    var card: String = "",
) : Serializable {
    val bgAlpha: Float
        get() = if (!StringUtils.isEmpty(card)) 0.5f else 1.0f
    val itemHasGet: Boolean
        get() = !StringUtils.isEmpty(card)

    val itemHasGetText: String
        get() = if (!StringUtils.isEmpty(card)) "已领取" else "立即领取"

}