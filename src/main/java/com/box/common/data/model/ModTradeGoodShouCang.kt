package com.box.common.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class ModTradeGoodShouCang(
    val gid: String = "",
    val add_time: String = "",
    val gameid: String = "",
    val goods_title: String = "",
    val goods_description: String = "",
    val goods_status: Int = 0,
    val fail_reason: String = "",
    val goods_price: String = "",
    val profit_rate: Float = 0f,
    val server_info: String,
    val uid: String = "",
    val trade_time: Long = 0L,
    val gamename: String = "",
    val gameicon: String = "",
    val game_suffix: String = "",
    ) : BaseObservable(),Serializable {


}

