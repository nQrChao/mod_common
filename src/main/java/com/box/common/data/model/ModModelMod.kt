package com.box.common.data.model

import java.io.Serializable

data class ModModelMod(
    val appName: String = "",
    val appVersionCode: Int = 0,
    val appVersionName: String = "",
    val appPackageName: String = "",
    val appIcon: String = "",
    val appUpdateId: String = "",
    val defaultTGID: String = "",
    val appApiVersion: String = "",
    val appFiling: String = "",
    val oChannelName: String = "",
    val signing: String = "",
    var icon: Int = 0,
) : Serializable {
}

