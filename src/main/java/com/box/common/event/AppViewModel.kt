package com.box.common.event

import androidx.lifecycle.MutableLiveData
import com.box.base.base.viewmodel.BaseViewModel
import com.box.common.data.model.AppUserInfo
import com.box.common.data.model.ProtocolInit
import com.box.common.data.model.ModMainTabConfig
import com.box.common.data.model.ModUserInfo
import com.box.common.data.model.ModUserRealName
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class AppViewModel : BaseViewModel() {
    var isLogin = false
    var oaid = ""
    var onBecameBackground = false

    var modMainTabConfig: UnPeekLiveData<MutableList<ModMainTabConfig>> =
        UnPeekLiveData.Builder<MutableList<ModMainTabConfig>>().setAllowNullValue(true).create()

    var appInfo: UnPeekLiveData<ProtocolInit> = UnPeekLiveData.Builder<ProtocolInit>().setAllowNullValue(true).create()

    var modUserInfo: UnPeekLiveData<ModUserInfo> =
        UnPeekLiveData.Builder<ModUserInfo>().setAllowNullValue(true).create()

    var modUserRealName: UnPeekLiveData<ModUserRealName> =
        UnPeekLiveData.Builder<ModUserRealName>().setAllowNullValue(true).create()

    var commonParams = MutableLiveData<MutableMap<String, String>>(mutableMapOf())

    var userInfo: UnPeekLiveData<AppUserInfo> =
        UnPeekLiveData.Builder<AppUserInfo>().setAllowNullValue(true).create()

}