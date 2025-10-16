package com.box.base.base.data

import com.box.base.state.ModResultState
import com.box.base.state.ModResultStateWithMsg
import com.box.common.data.model.AppletsLunTan
import com.box.common.data.model.RefundGames
import com.box.common.network.ModApiResponse
import com.box.common.network.NetworkApi
import com.box.common.network.apiService
import com.box.mod.modnetwork.ModAppException
import com.box.mod.modnetwork.ModExceptionHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 【最终版】数据仓库层
 * - 封装所有数据源的调用，向上层提供干净的数据接口。
 * - 内置了统一的异常处理和线程切换。
 */
@Singleton
class DataRepository @Inject constructor() {

    /**
     * 这是一个私有的网络请求“引擎”，所有公开方法都将调用它。
     * 它负责：
     * 1. 在 IO 线程执行网络请求。
     * 2. 捕获所有异常（网络、解析、业务等）。
     * 3. 将 Api 响应 [ModApiResponse] 智能地转换为 [ModResultState]。
     *
     * @param apiCall suspend 函数，代表实际的网络请求。
     * @return 封装好的 ModResultState<T>
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> ModApiResponse<T>): ModResultState<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSucceed()) {
                    ModResultState.Success(response.getResponseData())
                } else {
                    ModResultState.ModError(
                        ModAppException(
                            response.getResponseStatus(),
                            response.getResponseMsg()
                        )
                    )
                }
            } catch (e: Throwable) {
                ModResultState.ModError(ModExceptionHandle.handleException(e))
            }
        }
    }

    /**
     * 一个返回包含 msg 的 [ModResultStateWithMsg] 的 "引擎"
     */
    private suspend fun <T> safeApiCallWithMsg(apiCall: suspend () -> ModApiResponse<T>): ModResultStateWithMsg<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSucceed()) {
                    ModResultStateWithMsg.Success(response.getResponseData(), response.getResponseMsg())
                } else {
                    ModResultStateWithMsg.Error(
                        ModAppException(
                            response.getResponseStatus(),
                            response.getResponseMsg()
                        )
                    )
                }
            } catch (e: Throwable) {
                ModResultStateWithMsg.Error(ModExceptionHandle.handleException(e))
            }
        }
    }


    // --- 现在，所有的数据请求都变得极其简洁 ---

    suspend fun adActive(): ModResultState<Any?> {
        val map = mutableMapOf("api" to "ad_active")
        return safeApiCall { apiService.adActive(NetworkApi.INSTANCE.createPostData(map)!!) }
    }

    suspend fun refundGames(): ModResultState<RefundGames> {
        val map = mutableMapOf("api" to "refund_games")
        return safeApiCall { apiService.refundGames(NetworkApi.INSTANCE.createPostData(map)!!) }
    }

    suspend fun getZixun01(dataId: String): ModResultState<AppletsLunTan> {
        val map = mutableMapOf("api" to "market_data_appapi", "market_data_id" to dataId)
        return safeApiCall { apiService.postInfoLunTanAppApi(NetworkApi.INSTANCE.createPostData(map)!!) }
    }

    /**
     * 【新增】一个需要返回 msg 的请求示例
     */
    suspend fun getZixun01WithMsg(dataId: String): ModResultStateWithMsg<AppletsLunTan> {
        val map = mutableMapOf("api" to "market_data_appapi", "market_data_id" to dataId)
        return safeApiCallWithMsg { apiService.postInfoLunTanAppApi(NetworkApi.INSTANCE.createPostData(map)!!) }
    }


}