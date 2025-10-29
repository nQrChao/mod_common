package com.box.common

import com.box.com.BuildConfig
import com.box.common.data.model.DeviceInfoBean
import com.box.common.data.model.ModInitBean
import com.box.common.data.model.ModInfoBean
import com.box.common.data.model.ModUserInfo
import com.box.common.utils.mmkv.mmkv

object MMKVConfig {
    //  设备OAID
    var deviceOAID: String by mmkv("deviceOAID", "")
    //  设备信息
    var deviceInfos: DeviceInfoBean? by mmkv("deviceInfos", null)
    //  初始化信息，默认为 null。
    var modInit: ModInitBean? by mmkv("modInit", null)
    //  马甲信息
    var modInfos: ModInfoBean? by mmkv("modInfos", null)
    //  用户信息，默认为 null。
    var userInfo: ModUserInfo? by mmkv("userInfo", null)
    //  设备vasId
    var modVasId: String by mmkv("modVasId", BuildConfig.MOD_VASID)
    //  用户token
    var userToken: String by mmkv("userToken", "")

    // 用户隐私授权状态，默认为 false
    var permissionsUser: Boolean by mmkv("permissionsUser", false)
    // 设备相册获取授权状态，默认为 false
    var permissionsAlbum: Boolean by mmkv("permissionsAlbum", false)
    // 设备存储授权状态，默认为 false
    var permissionsStorage: Boolean by mmkv(" permissionsStorage", false)
    // 应用启动次数
    var launchCount: Int by mmkv("launch_count", 0)
    // 字体大小
    var fontScale: Float by mmkv("fontScale", 1.0f)


}


const val INTENT_KEY_INT = "intent_key_int"
const val INTENT_KEY_STRING = "intent_key_string"
const val INTENT_KEY_OUT_IMAGE_LIST: String = "imageList"
const val INTENT_KEY_OUT_IMAGE_ARRAYLIST: String = "imageArrayList"

const val RESULT_CODE_SELECT_PHOTO = 10090



