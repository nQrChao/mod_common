package com.box.common.data.model

import java.io.Serializable
data class AppletsBiZhiListData(
    val list_data: MutableList<AppletsBiZhi> = mutableListOf(),
) : Serializable {

}