package com.box.common.data.model

import androidx.databinding.BaseObservable
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

data class XDQYTradeApkGoodDetailBean(
    val title: String = "",
    val pic: String = "",
    val icon: String = "",
    val jump_target: String = "",
    val sub_title: String = "",
    val sort: String = "",
    val page_type: String = "",
    val param: Param,
    var viewType: Int = TYPE_NORMAL,
) : BaseObservable(), Serializable, MultiItemEntity {

    data class Param(
        val gameid: String = "",
    ) : Serializable

    override val itemType: Int
        get() = this.viewType

    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_TOP = 2
        const val TYPE_SMALL = 3
        const val TYPE_BIG = 4
        const val TYPE_LONG = 5
    }
}

