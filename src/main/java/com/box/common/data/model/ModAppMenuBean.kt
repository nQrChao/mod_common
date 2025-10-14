package com.box.common.data.model

import java.io.Serializable

data class ModAppMenuBean(
    val home_menu: List<ModAppMenu>?,
    val paihang_menu: List<ModAppMenu>?
) : Serializable {
}