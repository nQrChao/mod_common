package com.box.common.data

import com.box.common.data.model.ModValuationCommitBean
import com.box.other.blankj.utilcode.util.StringUtils
import java.io.Serializable

data class GameValuationCommitRequest(
    var checkState: Int = 0,
    var checkTime: String = "",
    var createTime: String = "",
    var characteristic: String = "",
    var fileNames: MutableList<String> = mutableListOf(),
    var froms: MutableList<ModValuationCommitBean> = mutableListOf(),
    var id: String = "",
    var gameId: String = "",
    var gameIcon: String = "",
    var gameName: String = "",
    var money: String = "",
    var userId: String = "",
) : Serializable{
    fun getCheckMoney():String{
        return when (checkState) {
            0 -> "审核中"
            2 -> "未通过"
            else ->{
                StringUtils.format("￥ %s", money)
            }
        }
    }
}

data class GameValuationCommitDetailRequest(
    val id: String,
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