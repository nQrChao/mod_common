package com.chaoji.im.data.model

import java.io.Serializable

data class AppletsBiZhi(
    val id: String = "",
    val title: String = "",
    val title2: String = "",
    val pic: String = "",
    val pic2: String = "",
    val pic3: String = "",
    val pic4: String = "",
    val redirect: String = "",
    val fabutime: String = "",
    val xiaobian: String = "",
    val shijian: String = "",
    val hits: String = "",
    var select: Boolean = false,
    var marketjson: AppletsBiZhiListData = AppletsBiZhiListData(),

) : Serializable