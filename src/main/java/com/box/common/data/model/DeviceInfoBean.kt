package com.box.common.data.model

import java.io.Serializable

data class DeviceInfoBean(
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
    var deviceManufacturer: String = "",
    var deviceSDKVersionName: String = "",
    var deviceSDKVersionCode: String = "",
    var devicePseudoID: String = "",

) :Serializable {
}

