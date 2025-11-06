package com.box.common.event

import androidx.lifecycle.MutableLiveData
import com.box.base.base.viewmodel.BaseViewModel
import com.box.common.data.model.ModInfoBean
import com.box.common.data.model.ModInitBean
import com.box.common.data.model.ModMainTabConfig
import com.box.common.data.model.ModUserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class AppViewModel : BaseViewModel() {
    var oaid = ""
    var modUserInfo: UnPeekLiveData<ModUserInfo> =
        UnPeekLiveData.Builder<ModUserInfo>().setAllowNullValue(true).create()
    var modInfoBean: UnPeekLiveData<ModInfoBean> =
        UnPeekLiveData.Builder<ModInfoBean>().setAllowNullValue(true).create()
    var modInitBean: UnPeekLiveData<ModInitBean> =
        UnPeekLiveData.Builder<ModInitBean>().setAllowNullValue(true).create()



    var onBecameBackground = false
    var modMainTabConfig: UnPeekLiveData<MutableList<ModMainTabConfig>> =
        UnPeekLiveData.Builder<MutableList<ModMainTabConfig>>().setAllowNullValue(true).create()

    var commonParams = MutableLiveData<MutableMap<String, String>>(mutableMapOf())


}