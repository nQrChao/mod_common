package com.chaoji.im.data.model

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject


/**
 * 共用的viewModel
 */
class ModModel : ViewModel() {
    val gamingTime: MutableMap<String, Int> = mutableMapOf()
    val gamingTimeSend = MutableLiveData<String>()
    //游戏账号状态
    val gameUserLoginOut = MutableLiveData<JsonObject>()

    val showLoading = MutableLiveData(View.GONE)


}