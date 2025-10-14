package com.chaoji.im.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class XDQYTradeGoodListBean(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val title_color: String = "",
    val sub_title: String = "",
    val sub_title_color: String = "",
    val type: Int = 0,
    val set_id: Int = 0,
    val title_icon: String = "",
    val description: String = "",
    val render_type: String = "",
    val render_directions: String = "",
    val data: MutableList<XDQYTradeGoodDetailBean> = mutableListOf(),
    val additional_data: MutableList<XDQYTradeGoodDetailBean> = mutableListOf(),
) : BaseObservable(), Serializable

