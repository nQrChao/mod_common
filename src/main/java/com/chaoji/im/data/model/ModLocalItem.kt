package com.chaoji.im.data.model

import java.io.Serializable

data class ModLocalItem(
    val id: Int = 0,
    val title: String = "",
    val title2: String = "",
    val pic: Int = 0,
    val bg: Int = 0,
) : Serializable