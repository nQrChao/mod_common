package com.box.common.data.model

import java.io.Serializable

data class ModInfosBean(
    var deviceModel: String = "",
    var deviceBRAND: String = "",
    var deviceVersionRelease: String = "",
    var deviceVersionSDKInt: String = "",
    var deviceSupportedABIS0: String = "",
    var deviceOAID: String = "",
    var deviceIMEI: String = "",
    var deviceGUID: String = "",
    var deviceCanvas: String = "",
    var deviceUniqueDeviceId: String = "",
    var deviceAndroidID: String = "",
    var deviceMacAddress: String = "",
    var modAPIVersion: String = "",
    var deviceManufacturer: String = "",
    var deviceSDKVersionName: String = "",
    var deviceSDKVersionCode: String = "",
    var devicePseudoID: String = "",
    var appName: String = "",
    var appPackageName : String = "",
    var modTencentId : String = "",
    var appSignaturesSHA1: String = "",
    var modId: String = "",
    var modName: String = "",
) :Serializable {
}

