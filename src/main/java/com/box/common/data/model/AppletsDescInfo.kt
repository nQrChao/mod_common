package com.box.common.data.model

import java.io.Serializable

class AppletsDescInfo(
    val id: String = "",
    val app_title: String = "",
    val app_title2: String = "",
    val app_title3: String = "",
    val app_title4: String = "",
    val app_title5: String = "",
    val app_title6: String = "",
    val list_data: MutableList<AppletsInfo> = mutableListOf(),
    val list_data2: MutableList<AppletsInfo> = mutableListOf(),
    val list_data3: MutableList<AppletsInfo> = mutableListOf(),
    val list_data4: MutableList<AppletsInfo> = mutableListOf(),
    val list_data5: MutableList<AppletsInfo> = mutableListOf(),
    val list_data6: MutableList<AppletsInfo> = mutableListOf(),
    ) : Serializable