package com.box.common.data.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class AppUserInfo(
    var uid: String,
    var username: String,
    var token: String = "",
    var auth: String = "",
    var mobile: String = "",

) : Parcelable
