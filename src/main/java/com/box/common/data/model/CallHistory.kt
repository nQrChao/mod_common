package com.box.common.data.model

import java.io.Serializable

class CallHistory : Serializable {
    var id: String? = null
    var userID: String? = null
    var nickname: String? = null
    var faceURL: String? = null
    var type: String? = null
    var success = false

    //失败状态 0连接中 1取消 2对方拒绝
    var failedState = 0
    var incomingCall = false
    var date: Long = 0
    var duration = 0

}