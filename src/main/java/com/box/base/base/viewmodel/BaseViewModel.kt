package com.box.base.base.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.box.base.base.AppScope
import com.box.base.callback.databind.BooleanObservableField
import com.box.base.callback.livedata.BooleanLiveData
import com.box.base.callback.livedata.event.EventLiveData
import com.box.base.ext.modAppRequest
import com.box.base.state.ModResultState
import com.box.common.appViewModel
import com.box.common.data.model.ModUserInfo
import com.box.common.data.model.RefundGames
import com.box.common.eventViewModel
import com.box.common.getOAIDWithCoroutines
import com.box.common.network.NetworkApi
import com.box.common.network.apiService
import com.box.common.utils.mmkv.MMKVConfig
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.immersionbar.BarHide
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.coroutines.cancellation.CancellationException

open class BaseViewModel(
    title: String = "",
    leftTitle: String = "",
    rightTitle: String = "",
    titleLine: Boolean = true,
    barHid: BarHide = BarHide.FLAG_SHOW_BAR,
    val isStatusBarEnabled: Boolean = true
) : ViewModel() {

    val leftClick = BooleanLiveData()
    fun leftClick() {
        leftClick.value = true
    }

    val showHeader = BooleanObservableField(title.length > 1 || leftTitle.length > 1 || rightTitle.length > 1)
    val barHidT = MutableLiveData(barHid)
    val titleLine = MutableLiveData(titleLine)
    val titleT = MutableLiveData(title)
    val leftTitleT = MutableLiveData(leftTitle)
    val rightTitleT = MutableLiveData(rightTitle)

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }
    var getDataResult = MutableLiveData<ModResultState<Any?>>()
    var refundGamesResult = MutableLiveData<ModResultState<RefundGames>>()

    fun isLogin(): Boolean {
        return eventViewModel.isLogin.value ?: false
    }

    fun userInfo(): ModUserInfo? {
        return appViewModel.modUserInfo.value
    }



    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { EventLiveData<String>() }

        //隐藏
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

    fun md5(string: String): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        val md5: MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(string.toByteArray())
            val result = java.lang.StringBuilder()
            for (b in bytes) {
                var temp = Integer.toHexString(b.toInt() and 0xff)
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getOAID() {
        AppScope.applicationScope.launch {
            try {
                //getCommonParams()
                val oaid = getOAIDWithCoroutines()
                appViewModel.oaid = oaid
                MMKVConfig.deviceOAID = oaid
                Logs.e("getOAIDWithCoroutines---getOAID:$oaid")
            } catch (e: CancellationException) {
                Logs.e("Coroutine was cancelled. This is why adActive() was not called.", e)
                throw e
            } catch (e: Throwable) {
                Logs.e("getOAID process failed with a non-cancellation error", e)
            } finally {
                withContext(NonCancellable) {
                    postGetData()
                    Logs.d("Running final, non-cancellable actions.")
                }
            }
        }
    }

    suspend fun postGetData() {
        modAppRequest({
            val map = mutableMapOf<String, String>()
            map["api"] = "ad_active"
            apiService.getData(NetworkApi.INSTANCE.createPostData(map)!!)
        }, getDataResult)
    }


    /*******************************************************************/



    /*******************************************************************/

}