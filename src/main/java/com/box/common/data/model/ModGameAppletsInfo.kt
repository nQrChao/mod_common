package com.box.common.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class ModGameAppletsInfo(
    val gameId: String = "",
    val gameName: String = "",
    val gameDesc: String = "",
    val gameIcon: String = "",
    val genre_str: String = "",
    val vip_label: String = "",
    val pingtaibi_str: String = "",
    val discount_str: String = "",
) : BaseObservable(), Serializable