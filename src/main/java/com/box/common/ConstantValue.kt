package com.box.common

import android.Manifest
import com.box.other.blankj.utilcode.util.PathUtils
import com.hjq.permissions.Permission

//最大游戏数
const val COUNT_GAME_MAX = 3
const val GAME_CHENGYUCAICAICAI = "file:///android_asset/game/chengyucaicaicai/index.html"
const val GAME_KAIXINYIXIA = "file:///android_asset/game/kaixinyixia/index.html"
const val GAME_JIANFENGCHAZHEN = "file:///android_asset/game/jianfengchazhen/index.html"
const val GAME_CAICAIDIANYINGMING = "file:///android_asset/game/caicaidianyingming/index.html"
const val GAME_CAIMIYU = "file:///android_asset/game/caimiyu/index.html"

//支付订单自动查询次数
const val COUNT_ORDER_QUERY = 10
//HTTP 读取耗时,秒
const val TIME_READ_TIMEOUT = 60L
//HTTP 写入耗时,秒
const val TIME_WRITE_TIMEOUT = 60L
//HTTP 连接超时,秒
const val TIME_CONNECT_TIMEOUT = 30L
//上报角色间隔时长,秒
const val TIME_ROLE_INTERVAL = 10
//防沉迷后自动退出倒计时,秒
const val TIME_ANTI_ADDICTION_EXIT = 15
//添加的注释总长度
const val APK_CHANNEL_LENGTH = 40
//添加的注释文件名称
const val NAME_META_INF_CHANNEL = "/META-INF/box"
//截屏
const val SCREEN_SHOT = 0
//录屏
const val SCREEN_RECORDING = 1
//开始录屏
const val SCREEN_RECORD_TYPE_START = 0
const val APK_CHANNEL_INFO = "apkChannel"
//语言
const val K_LANGUAGE_SP = "language_sp"

//自定义消息类型
const val K_CUSTOM_TYPE = "customType"

const val LOADING = 201

const val REMIND_FRIENDS = "remind_friends"
const val REMIND_GROUPS = "remind_groups"
const val REMIND_SYSTEM = "remind_system"
const val REMIND_FRIENDS_S = "remind_friends_s"
const val REMIND_GROUPS_S = "remind_groups_s"

//文件夹
val File_DIR: String = PathUtils.getInternalAppFilesPath()

val LOCATIONPermission =
    arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

val CAMERAPermission =
    arrayOf(
        Permission.CAMERA,
        Permission.RECORD_AUDIO,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_EXTERNAL_STORAGE
    )

val AUDIOPermission =
    arrayOf(
        Permission.RECORD_AUDIO,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_EXTERNAL_STORAGE
    )

val STORAGEPermission =
    arrayOf(
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_EXTERNAL_STORAGE,
    )

val VIDEOPermission =
    arrayOf(
        Permission.CAMERA,
        Permission.RECORD_AUDIO,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_EXTERNAL_STORAGE
    )



