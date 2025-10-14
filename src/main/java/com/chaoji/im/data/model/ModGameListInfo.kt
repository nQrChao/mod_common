package com.chaoji.im.data.model

import java.io.Serializable

data class ModGameListInfo(
    val id: String = "",
    val title: String = "",
    val title2: String = "",
    val pic: String = "",
    val pic2: String = "",
    val redirect: String = "",
    var marketjson: ListData = ListData()
) : Serializable {
    data class ListData(
        val list_data: MutableList<ModGameAppletsInfo> = mutableListOf(),
    )
}