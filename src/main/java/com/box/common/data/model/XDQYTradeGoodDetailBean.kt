package com.box.common.data.model

import androidx.databinding.BaseObservable
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

data class XDQYTradeGoodDetailBean(
    val pic: String = "",
    val icon: String = "",
    val title: String = "",
    val sub_title: String = "",
    val sort: String = "",
    val jump_target: String = "",
    val gameid: String = "",
    val gamename: String = "",
    val summary: String = "",
    val game_icon: String = "",
    val client_dir: String = "",
    val play_count: String = "",
    val video: String = "",
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
        const val TYPE_VIDEO = 2
        const val TYPE_TITLE = 3
    }
}

