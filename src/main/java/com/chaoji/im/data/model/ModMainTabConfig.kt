package com.chaoji.im.data.model

import java.io.Serializable

data class ModMainTabConfig(
    val title: String,
    val normalIcon: Int,
    val normalIconUrl: String ="",
    val selectedIcon: Int,
    val selectedIconUrl: String ="",
    val fragmentId: Int,
    val titleNormalColor: String ="",
    val titleSelectedColor: String ="",
    ) : Serializable