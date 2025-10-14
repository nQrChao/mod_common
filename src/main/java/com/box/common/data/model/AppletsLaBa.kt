package com.box.common.data.model

import java.io.Serializable

data class AppletsLaBa(
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
    val hits: String = "",
    val shijian: String = "",
    var select: Boolean = false,
    var marketjson: ListData = ListData(),
    var type:Int = 1,

) :  Serializable {

    data class ListData(
        val list_data: MutableList<String> = mutableListOf(),
    )


}