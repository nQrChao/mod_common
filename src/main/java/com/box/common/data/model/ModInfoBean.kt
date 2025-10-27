package com.box.common.data.model

import java.io.Serializable

data class ModInfoBean(
    var appName: String = "",
    var appPackageName : String = "",
    var appVersionName : String = "",
    var appVersionCode : String = "",
    var appSignaturesMD5: String = "",
    var appSignaturesSHA1: String = "",
    var modId: String = "",
    var modName: String = "",
    var modVasDollyId : String = "",
    var modAPIVersion: String = "",
    var systemId: String = "",
) :Serializable {
}

