package com.box.common.data.model

import java.io.Serializable

data class AppletsXiaoGame(
    val id: String = "",
    val title: String = "",
    val title2: String = "",
    val pic: String = "",
    val pic2: String = "",
    val pic3: String = "",
    val pic4: String = "",
    val redirect: String = "",
    val fabutime: String = "",
    val xiaobian: String = "",
    var marketjson: ListData = ListData()

) : Serializable {
    data class ListData(
        val url_coupon648: String = "",
        val url_gold_game: String = "",
        val coupon_count: String = "",
        val ptb_dc: String = "",
        val list_data: MutableList<AppletsXiaoGameBean> = mutableListOf(),
    )

}