package com.box.common.data.model

import android.content.res.ColorStateList
import java.io.Serializable

data class AppletsInfo(
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
    val shijian: String = "",
    var dianzan: String = "",
    val app_title: String = "",
    var hits: String = "",
    var content: String = "",
    var need_replace: String = "",
    var isSelect:Boolean = false,
    var marketjson: ListData = ListData()

) : Serializable {
    val highlightColor: Int
        get() = if (isSelect) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt()
    val highlightColorStateList: ColorStateList
        get() = ColorStateList.valueOf(if (isSelect) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt())
    data class ListData(
        val title: String = "",
        val app_title: String = "",
        val app_title2: String = "",
        val list_data: MutableList<AppletsInfo> = mutableListOf(),
        val list_data2: MutableList<AppletsInfo> = mutableListOf(),
    )

}