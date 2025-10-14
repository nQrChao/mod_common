package com.chaoji.im.event

import androidx.lifecycle.MutableLiveData
import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.im.data.model.AppUserInfo
import com.chaoji.im.data.model.AppletsInfo
import com.chaoji.im.data.model.RemindBean
import com.chaoji.im.data.model.AppletsData
import com.chaoji.im.data.model.ModMainTabConfig
import com.chaoji.im.data.model.ModUserInfoBean
import com.chaoji.im.data.model.ModUserRealName
import com.chaoji.im.utils.MMKVUtil
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class AppViewModel : BaseViewModel() {
    var isLogin = false
    var deviceId = ""
    var oaid = ""
    var deviceMac = ""
    var deviceAndroidId = ""

    val cnOAID = MutableLiveData<String>()
    val cnGUID = MutableLiveData<String>()

    var modMainTabConfig: UnPeekLiveData<MutableList<ModMainTabConfig>> =
        UnPeekLiveData.Builder<MutableList<ModMainTabConfig>>().setAllowNullValue(true).create()


    var appInfo: UnPeekLiveData<AppletsData> = UnPeekLiveData.Builder<AppletsData>().setAllowNullValue(true).create()

    var modUserInfo: UnPeekLiveData<ModUserInfoBean> =
        UnPeekLiveData.Builder<ModUserInfoBean>().setAllowNullValue(true).create()

    var modUserRealName: UnPeekLiveData<ModUserRealName> =
        UnPeekLiveData.Builder<ModUserRealName>().setAllowNullValue(true).create()

    var appLetsList: UnPeekLiveData<MutableList<AppletsInfo>> =
        UnPeekLiveData.Builder<MutableList<AppletsInfo>>().setAllowNullValue(true).create()

    var commonParams = MutableLiveData<MutableMap<String, String>>(mutableMapOf())

    var messageActivityId: UnPeekLiveData<String> =
        UnPeekLiveData.Builder<String>().setAllowNullValue(true).create()

    var onBecameBackground = false

    var appUserInfo: UnPeekLiveData<AppUserInfo> =
        UnPeekLiveData.Builder<AppUserInfo>().setAllowNullValue(true).create()

    var userInfo: UnPeekLiveData<AppUserInfo> =
        UnPeekLiveData.Builder<AppUserInfo>().setAllowNullValue(true).create()

    //val onNewMessage = MutableLiveData<Message>()

    val imChatTwoLevel = MutableLiveData<Boolean>()

    var remindList: MutableList<RemindBean> = MMKVUtil.getRemindList()

    // 后台运行的applets   用于存储正在运行的应用信息
    val currentApplets = MutableLiveData<AppletsInfo>()
    var appletsList: MutableList<AppletsInfo> = mutableListOf()
    val addGameEvent = MutableLiveData<AppletsInfo>()
    fun addGame(applets: AppletsInfo) {
//        if (appletsList.size == 3 && !appletsList.contains(applets)) {
//            Toaster.show("最多开启3个")
//            return
//        }
        addGameEvent.postValue(applets)
    }

    // H5box是否弹出输入法  true弹出  false禁用(为了隐藏导航栏)
    val h5boxSoftInputMode = MutableLiveData<Boolean>()
    fun isOwner(userId: String): Boolean {
        return userId == userInfo.value?.uid
    }

    fun getUserId(): String {
        return userInfo.value?.uid!!
    }

}