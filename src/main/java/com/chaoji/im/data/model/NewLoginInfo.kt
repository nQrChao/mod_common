package com.chaoji.im.data.model

data class NewLoginInfo(
    var dateType: Int,
    val jwtRefreshToken: String,
    val jwtToken: String,
    var data: NewLoginData
)
