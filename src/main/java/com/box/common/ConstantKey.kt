package com.box.common

import com.box.com.BuildConfig
import com.box.common.data.model.DeviceInfoBean
import com.box.common.data.model.ModDataBean
import com.box.common.data.model.ModInfoBean
import com.box.common.data.model.ModInitBean
import com.box.common.data.model.ModUserInfo
import com.box.common.utils.logsE
import com.box.common.utils.mmkv.mmkv
import com.box.common.utils.mmkv.mmkvList

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

    //  生成的角色名
    val randomNameList: MutableList<ModDataBean> = mmkvList("randomNameList")
    fun addRandomNameList(toAdd: ModDataBean) {
        val existingIndex = randomNameList.indexOfFirst { it.name == toAdd.name }
        if (existingIndex != -1) {
            randomNameList[existingIndex] = toAdd
        } else {
            randomNameList.add(toAdd)
        }
    }
    fun removeRandomNameList(remove: ModDataBean) {
        val indexToRemove = randomNameList.indexOfFirst { it.name == remove.name }
        if (indexToRemove != -1) {
            randomNameList.removeAt(indexToRemove)
        }
    }
    fun getRandomName(): MutableList<ModDataBean> {
        return randomNameList.toMutableList()
    }

    fun getShouCangNameList(): MutableList<ModDataBean> {
        return randomNameList.filter { it.isShouCang }.toMutableList()
    }

    fun updateRandomNameStatusByName(nameToFind: String, newShouCangStatus: Boolean) {
        val indexToUpdate = randomNameList.indexOfFirst { it.name == nameToFind }
        if (indexToUpdate != -1) {
            val currentItem = randomNameList[indexToUpdate]
            if (currentItem.isShouCang == newShouCangStatus) {
                return
            }
            val updatedItem = currentItem.copy(isShouCang = newShouCangStatus)
            randomNameList[indexToUpdate] = updatedItem
        } else {
            logsE("updateShouCangStatus: 未找到 name 为 $nameToFind 的项")
        }
    }

    //  小游戏排行榜
    val gameRankList: MutableList<ModDataBean> = mmkvList("gameRankList")
    fun addGameRankList(toAdd: ModDataBean) {
        val existingIndex = gameRankList.indexOfFirst { it.name == toAdd.name }
        if (existingIndex != -1) {
            gameRankList[existingIndex] = toAdd
        } else {
            gameRankList.add(toAdd)
        }
    }

    fun getGameRank(): MutableList<ModDataBean> {
        return gameRankList.toMutableList()
    }
    fun removeGameRankList(remove: ModDataBean) {
        val indexToRemove = gameRankList.indexOfFirst { it.name == remove.name }
        if (indexToRemove != -1) {
            gameRankList.removeAt(indexToRemove)
        }
    }

    fun updateGameRankByName(nameToFind: String, newShouCangStatus: Boolean) {
        val indexToUpdate = gameRankList.indexOfFirst { it.name == nameToFind }
        if (indexToUpdate != -1) {
            val currentItem = gameRankList[indexToUpdate]
            if (currentItem.isShouCang == newShouCangStatus) {
                return
            }
            val updatedItem = currentItem.copy(isShouCang = newShouCangStatus)
            gameRankList[indexToUpdate] = updatedItem
        } else {
            logsE("updateShouCangStatus: 未找到 name 为 $nameToFind 的项")
        }
    }

}


const val INTENT_KEY_INT = "intent_key_int"
const val INTENT_KEY_STRING = "intent_key_string"
const val INTENT_KEY_OUT_IMAGE_LIST: String = "imageList"
const val INTENT_KEY_OUT_IMAGE_ARRAYLIST: String = "imageArrayList"

const val RESULT_CODE_SELECT_PHOTO = 10090



