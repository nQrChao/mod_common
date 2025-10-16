package com.box.common.data.model

import com.box.com.R
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
    fun getRankIcon(): Int {
        return when (rank) {
            1 -> R.drawable.mod_rank_top1
            2 -> R.drawable.mod_rank_top2
            3 -> R.drawable.mod_rank_top3
            else ->
                android.R.color.transparent
        }
    }

    fun isShowRankIcon(): Boolean {
        return rank < 4
    }
}

