package com.box.common.data

import java.io.Serializable

data class RegisterRequest(
    val userName: String,
    val password: String
): Serializable

data class ChangePasswordRequest(
    val newPwd: String,
    val oldPwd: String
): Serializable

data class DeleteUserRequest(
    val pwd: String,
): Serializable

data class AndroidStatusRequest(
    val appId: String = "",
    val city: String = "",
    val country: String = "",
    val deviceAndroidID: String = "",
    val deviceCanvas: String = "",
    val deviceGUID: String = "",
    val deviceIMEI: String = "",
    val deviceOAID: String = "",
    val deviceUniqueDeviceId: String = "",
    val pageNum: Int = 1,
    val pageSize: Int = 10
) : Serializable