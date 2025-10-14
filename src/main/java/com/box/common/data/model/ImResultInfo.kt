package com.box.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImResultInfo(
    var imToken: String,
    var chatToken: String,
    var userID: String,
) : Parcelable
