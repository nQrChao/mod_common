package com.chaoji.im.data.model

import java.io.Serializable

class PayInfo(var type: Int = -1, var name: String = "", var icon: Int = 0, var desc: String = "描述", var isChoice: Boolean = false) : Serializable