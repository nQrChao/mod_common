package com.box.common.data

import com.box.common.data.model.ModValuationCommitBean
import java.io.Serializable

data class GameValuationCommitRequest(
    val checkState: Int,
    val checkTime: String,
    val createTime: String,
    val desc: String,
    val fileNames: MutableList<String>,
    val froms: MutableList<ModValuationCommitBean>,
    val id: Int,
    val money: String,
    val userId: String,
) : Serializable

data class RegisterRequest(
    val userName: String,
    val password: String
) : Serializable

data class ChangePasswordRequest(
    val newPwd: String,
    val oldPwd: String
) : Serializable

data class DeleteUserRequest(
    val pwd: String,
) : Serializable

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