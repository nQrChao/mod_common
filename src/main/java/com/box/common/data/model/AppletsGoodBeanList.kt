package com.box.common.data.model

import java.io.Serializable

data class AppletsGoodBeanList(
    val id: String = "",
    val title: String = "",
    val title2: String = "",
    val pic: String = "",
    val pic2: String = "",
    val redirect: String = "",
    var marketjson: ListData = ListData()
) : Serializable {
    data class ListData(
        val limit: Int = 0,
        val list_data: MutableList<ModTradeGoodBean> = mutableListOf(),
    )

}