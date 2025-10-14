package com.chaoji.im.data.model

import java.io.Serializable

data class ModLoginBean(
    val uid: Int = 0,
    val username: String = "",
    val is_special: Int = 0,
    val tgid: String = "",
    val token: String = "",
    val auth: String = "",
    val elevate: Int = 0,
    val is_regular: Int = 0,
    val recall_pop: Int = 0,
    val act: String = "",
    ) : Serializable