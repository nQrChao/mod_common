package com.chaoji.im.data.model

import java.io.Serializable

class AppletsMeta(
    var page: Int = 0,
    var perPage: Int = 0,
    var total: Int = 0,
    var hasPages: Boolean = false
) : Serializable