package com.chaoji.im.data.model

import java.io.Serializable

class NotificationMsg : Serializable {
    var opUser: NotificationOpUser? = null
    var group: NotificationContent? = null
}