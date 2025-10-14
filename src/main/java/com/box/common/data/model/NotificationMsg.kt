package com.box.common.data.model

import java.io.Serializable

class NotificationMsg : Serializable {
    var opUser: NotificationOpUser? = null
    var group: NotificationContent? = null
}