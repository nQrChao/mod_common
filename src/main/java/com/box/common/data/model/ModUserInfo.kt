package com.box.common.data.model

import java.io.Serializable

data class ModUserInfo(
    val accountName: String = "",
    val userId: String = "",
    var userToken: String = "",
    val userAuthLoginToken: String = "",
    val userVasDollyId: String = "",
    val userAvatar: String = "",
    val userNickName: String = "",
    val userMobile: String = "",
    val userEmail: String = "",
    val userBirthDay: String = "",
    val userRealName: String = "",
    val userRealNameNumber: String = "",
    val userLevel: String = "",
    val userVipLevel: String = "",
    val canBindMobile: String = "",
    val canBindAccountName: String = "",
    val canBindPassword: String = "",
) : Serializable {

}

