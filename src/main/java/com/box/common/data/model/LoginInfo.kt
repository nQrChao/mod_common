package com.box.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginInfo(
    var ID: Int,
    var status: Int,
    var accounts_name: String,
    var phone: String,
    var accounts_models: String
) : Parcelable
