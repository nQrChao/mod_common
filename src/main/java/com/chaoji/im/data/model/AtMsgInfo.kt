package com.chaoji.im.data.model

import java.io.Serializable

class AtMsgInfo : Serializable {
    var text: String? = null
    var atUserList: List<String>? = null
    var isAtSelf = false
}