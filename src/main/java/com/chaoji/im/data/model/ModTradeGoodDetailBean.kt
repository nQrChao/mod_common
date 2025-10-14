package com.chaoji.im.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class ModTradeGoodDetailBean(
    val gid: String = "",
    val uid: Int = 0,
    val gameid: String = "",
    val game_type: String = "",
    val goods_title: String = "",
    val goods_price: String = "",
    val goods_status: Int = 0,
    val gamename: String = "",
    val game_suffix: String = "",
    val gameicon: String = "",
    val goods_pic: String = "",
    val trade_time: Long = 0L,
    val verify_time: String = "",
    val show_time: String = "",
    val fail_reason: String = "",
    val game_is_close: Int = 0,
    val purchase_uid: Int = 0,
    val is_seller: Int = 0,
    val count_down: Int = 0,
    val goods_type: String = "",
    val isSelled: Int = 0,
    val can_bargain: Int = 0,
    val auto_price: Int = 0,
    val endTime: Long = 0L,
    val xh_username: String = "",
    val xh_showname: String = "",
    val xh_client: Int = 0,
    val create_days: Int = 0,
    val is_favorite: Int = 0,
    val xh_pay_total: Float = 0f,
    val xh_pay_game_total: String = "",
    val xh_ctime: Long = 0L,
    val xh_passwd: String = "",
    val profit_rate: Float = 0f,
    val server_info: String = "",
    val genre_str: String = "",
    val play_count: String = "",
    val goods_description: String = "",
    val client_type: String = "",
    val data_exchange: String = "",
    val pic: MutableList<Pic> = mutableListOf(),
    val pic_list: MutableList<PicList> = mutableListOf(),
    var localSaveTime: String = ""
) : BaseObservable(), Serializable {

    data class Pic(
        val pic_path: String = "",
    ) : Serializable

    data class PicList(
        val pid: String = "",
        val gid: String = "",
        val pic_path: String = "",
        val pic_width: String = "",
        val pic_height: String = "",
    ) : Serializable
}

