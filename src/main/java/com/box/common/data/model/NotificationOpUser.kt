package com.box.common.data.model

import java.io.Serializable

class NotificationOpUser : Serializable {
    var groupID: String = ""
    var userID: String = ""
    var roleLevel = 0
    var joinTime = 0L
    var nickname: String = ""
    var joinSource = 0
    var operatorUserID: String = ""
    var inviterUserID: String = ""
}