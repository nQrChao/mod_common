package com.chaoji.im.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImCheckResultInfo(
    var status: Int = 0,
    var msg: String = "",
) : Parcelable
