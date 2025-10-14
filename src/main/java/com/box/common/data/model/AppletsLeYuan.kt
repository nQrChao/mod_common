package com.box.common.data.model

import android.content.res.ColorStateList
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.box.common.R
import com.smart.adapter.interf.SmartFragmentTypeExEntity

data class AppletsLeYuan(
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
    val gamename: String = "",
    val gameicon: String = "",
    val game_description: String = "",
    val video_pic: String = "",
    val video_url: String = "",
    val play_count: String = "",
    var select: Boolean = false,
    var dianzan: String = "",
    var hits: String = "",
    var marketjson: ListData = ListData(),
    var type: Int = 1,
    var viewType: Int = TYPE_NORMAL,
) : SmartFragmentTypeExEntity() , MultiItemEntity {
    fun getVideoTabBg(): Int {
        return if (select) {
            R.drawable.mod_item_video_tab_bg2
        } else {
            R.drawable.mod_item_video_tab_bg1
        }
    }
    val videoTabTextColor: Int
        get() = if (select) 0xFF000000.toInt() else 0xFF8D8D8D.toInt()

    fun getPicTopBg(): Int {
        return if (select) {
            R.drawable.mod_bg_pic_top_select_bg
        } else {
            R.drawable.mod_bg_pic_top_unselect_bg
        }
    }

    val picTopTextColor: Int
        get() = if (select) 0xFF000000.toInt() else 0xFFF24677.toInt()

    val highlightColor: Int
        get() = if (select) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt()
    val highlightColorStateList: ColorStateList
        get() = ColorStateList.valueOf(if (select) 0xFF2494F1.toInt() else 0xFFD5D5D5.toInt())

    data class ListData(
        val list_data: MutableList<AppletsLeYuan> = mutableListOf(),
    )

    override fun getFragmentType(): Int {
        return type
    }

    override val itemType: Int
        get() = this.viewType
    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_LARGE_IMAGE = 2
        const val TYPE_GRID_ITEM = 3
        const val TYPE_TITLE = 4
    }

}