package com.box.common

import com.box.common.data.model.ModUserInfoBean
import com.box.common.utils.mmkv.MMKVDelegate
import com.box.common.utils.mmkv.mmkv

object MMKVConfig {
    // 用户信息，默认为 null。可以是可空类型
    var modInit: ModUserInfoBean? by mmkv("USER_INFO", null)
    // 授权状态，默认为 false
    var shouQuan: Boolean by mmkv("SHOU_QUAN", false)
    // 存储授权状态，默认为 false
    var EXTERNAL_STORAGE: Boolean by mmkv("EXTERNAL_STORAGE", false)
    // 用户信息，默认为 null。可以是可空类型
    var userInfo: ModUserInfoBean? by mmkv("USER_INFO", null)
    // 启动次数
    var launchCount: Int by mmkv("launch_count", 0)
    // 字体大小
    var fontScale: Float by mmkv("fontScale", 1.0f)
}


const val INTENT_KEY_STRING = "intent_key_string"
const val INTENT_KEY_OUT_IMAGE_LIST: String = "imageList"
const val INTENT_KEY_OUT_IMAGE_ARRAYLIST: String = "imageArrayList"




