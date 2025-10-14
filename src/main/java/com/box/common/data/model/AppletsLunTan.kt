package com.box.common.data.model

import android.content.res.ColorStateList
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.box.common.R
import com.smart.adapter.interf.SmartFragmentTypeExEntity

data class AppletsLunTan(
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
    var select: Boolean = false,
    var marketjson: ListData = ListData(),
    var viewType: Int = TYPE_NORMAL,
    var type:Int = 1,

) :  SmartFragmentTypeExEntity(), MultiItemEntity {
    fun getPicTopBg(): Int {
        return if (select) {
            R.drawable.mod_bg_pic_top_select_bg
        } else {
            R.drawable.mod_bg_pic_top_unselect_bg
        }
    }
    val picTopTextColor: Int
        get() = if (select) 0xFFF24677.toInt() else 0xFF000000.toInt()

    val highlightColor: Int
        get() = if (select) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt()
    val highlightColorStateList: ColorStateList
        get() = ColorStateList.valueOf(if (select) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt())

    data class ListData(
        val list_data: MutableList<AppletsLunTan> = mutableListOf(),
    )

    override val itemType: Int
        get() = this.viewType
    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_LARGE_IMAGE = 2
        const val TYPE_GRID_ITEM = 3
        const val TYPE_TITLE = 4
    }

    override fun getFragmentType(): Int {
        return type
    }
}