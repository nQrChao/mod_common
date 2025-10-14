package com.box.common.data.model

import java.io.Serializable

class MeetingInfo : Serializable {
    var inviterNickname: String? = null
    var id: String? = null
    var subject: String? = null
    var start: Long = 0
    var startTime: String? = null
    var duration = 0
    var durationStr: String? = null

    var inviterFaceURL: String? = null
    var inviterUserID: String? = null
}