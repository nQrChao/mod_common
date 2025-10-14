package com.box.common.data.model

data class NewLoginInfo(
    var dateType: Int,
    val jwtRefreshToken: String,
    val jwtToken: String,
    var data: NewLoginData
)
