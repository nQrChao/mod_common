package com.chaoji.im.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class XDQYInitBean(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val icon_active: String = "",
    val api: String = "",
    val page_type: String = "",
    val param: Param,
) : BaseObservable(), Serializable{
    data class Param(
        val page_id: String = "",
    ) : Serializable
}

