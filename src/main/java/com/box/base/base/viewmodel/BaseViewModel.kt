package com.box.base.base.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.box.base.base.AppScope
import com.box.base.callback.databind.BooleanObservableField
import com.box.base.callback.livedata.BooleanLiveData
import com.box.base.callback.livedata.event.EventLiveData
import com.box.base.ext.modAppRequest
import com.box.base.ext.modRequest
import com.box.base.state.ModResultState
import com.box.common.appContext
import com.box.common.appViewModel
import com.box.common.data.model.AppletsLunTan
import com.box.common.data.model.RefundGames
import com.box.common.getOAIDWithCoroutines
import com.box.common.network.NetworkApi
import com.box.common.network.apiService
import com.box.mod.game.ModComService
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
    var adActiveResult = MutableLiveData<ModResultState<Any?>>()
    var refundGamesResult = MutableLiveData<ModResultState<RefundGames>>()

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
                val oaid = getOAIDWithCoroutines(appContext)
                appViewModel.oaid = oaid
                Logs.e("getOAIDWithCoroutines---getOAID:$oaid")
            } catch (e: CancellationException) {
                Logs.e("Coroutine was cancelled. This is why adActive() was not called.", e)
                throw e
            } catch (e: Throwable) {
                Logs.e("getOAID process failed with a non-cancellation error", e)
            } finally {
                withContext(NonCancellable) {
                    refundGames()
                    refundGames()
                    adActive()
                    Logs.d("Running final, non-cancellable actions.")
                }
            }
        }
    }

    suspend fun adActive() {
        modAppRequest({
            val map = mutableMapOf<String, String>()
            map["api"] = "ad_active"
            apiService.adActive(NetworkApi.INSTANCE.createPostData(map)!!)
        }, adActiveResult)
    }

    suspend fun refundGames() {
        modAppRequest({
            val map = mutableMapOf<String, String>()
            map["api"] = "refund_games"
            apiService.refundGames(NetworkApi.INSTANCE.createPostData(map)!!)
        }, refundGamesResult)
    }

    /*******************************zixun01************************************/

    var postZixun01Result = MutableLiveData<ModResultState<AppletsLunTan>>()
    fun postZixun01(dataId: String) {
        modRequest({
            val map = mutableMapOf<String, String>()
            map["api"] = "market_data_appapi"
            map["market_data_id"] = dataId
            apiService.postInfoLunTanAppApi(NetworkApi.INSTANCE.createPostData(map)!!)
        }, postZixun01Result)
    }

    fun getZixun01PicUrlList(appletsLunTanList: MutableList<AppletsLunTan>): MutableList<String> {
        val picUrlList = appletsLunTanList.map { it.pic }.toMutableList()
        return picUrlList
    }

    /*******************************zixun02************************************/

}