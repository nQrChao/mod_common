package com.box.common.data.model

import android.content.res.ColorStateList
import java.io.Serializable

data class ModInfo(
    val id: String = "",
    val title: String = "",
    val title1: String = "",
    val title2: String = "",
    val title3: String = "",
    val title4: String = "",
    val title5: String = "",
    val pic: String = "",
    val pic1: String = "",
    val pic2: String = "",
    val pic3: String = "",
    val pic4: String = "",
    val pic5: String = "",
    val desc: String = "",
    val desc1: String = "",
    val desc2: String = "",
    val desc3: String = "",
    val desc4: String = "",
    val desc5: String = "",
    val time: String = "",
    val time1: String = "",
    val time2: String = "",
    val time3: String = "",
    val time4: String = "",
    val time5: String = "",
    var isSelect:Boolean = false,
    var marketjson: ListData = ListData()

) : Serializable {
    val highlightColor: Int
        get() = if (isSelect) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt()
    val highlightColorStateList: ColorStateList
        get() = ColorStateList.valueOf(if (isSelect) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt())
    data class ListData(
        val title: String = "",
        val list_data: MutableList<ModInfo> = mutableListOf(),
        val list_data1: MutableList<ModInfo> = mutableListOf(),
        val list_data2: MutableList<ModInfo> = mutableListOf(),
        val list_data3: MutableList<ModInfo> = mutableListOf(),
        val list_data4: MutableList<ModInfo> = mutableListOf(),
        val list_data5: MutableList<ModInfo> = mutableListOf(),
    )

}