package com.chaoji.im.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AiResultInfo(
    var jwtToken: String = "",
    var refresh: String = "",
    var userId: String = ""
) : Parcelable
