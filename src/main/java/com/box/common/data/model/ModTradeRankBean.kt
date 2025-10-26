package com.box.common.data.model

import java.io.Serializable


data class ModTradeRankBean(
    var rank: Int = 0,
    val gameid: String = "",
    val gamename: String = "",
    val gameicon: String = "",
    val tradecount: String = "",
) : Serializable {
    fun getRankText():String{
        return rank.toString()
    }

    fun isShowRankIcon(): Boolean {
        return rank < 4
    }
}

