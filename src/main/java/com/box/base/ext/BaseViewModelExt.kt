package com.box.base.ext

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.box.base.base.activity.BaseModVmDbActivity
import com.box.base.base.fragment.BaseVmDbFragment
import com.box.base.base.viewmodel.BaseViewModel
import com.box.mod.modnetwork.ModAppException
import com.box.mod.modnetwork.ModExceptionHandle
import com.box.base.network.AppException
import com.box.base.network.BaseResponse
import com.box.base.network.ExceptionHandle
import com.box.base.state.ModResultState
import com.box.base.state.ModResultStateWithMsg
import com.box.base.state.ResultState
import com.box.base.state.paresException as paresModException // 为ModResultState的扩展重命名
import com.box.base.state.paresResult as paresModResult     // 为ModResultState的扩展重命名
import com.box.base.state.paresException
import com.box.base.state.paresResult
import com.box.base.utils.loge
import com.box.common.getOAIDWithCoroutines
import com.box.common.network.ModApiResponse
import com.box.common.network.apiService
import com.box.common.network.initializeNetwork
import com.box.common.utils.MMKVUtil
import com.box.other.blankj.utilcode.util.Logs
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// =================================================================================================
// ===                                    UI状态解析扩展 (保留)                                   ===
// =================================================================================================

/**
 * [UI辅助] 在 Activity 中解析并处理 ResultState 的状态。
 */
fun <T> BaseModVmDbActivity<*, *>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T?) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }

        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }

        is ResultState.Error -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}

/**
 * 在 Activity 中解析并处理 ModResultState 的状态。
 */
fun <T> BaseModVmDbActivity<*, *>.parseModState(
    resultState: ModResultState<T>,
    onSuccess: (T?) -> Unit,
    onError: ((ModAppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ModResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }

        is ModResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }

        is ModResultState.ModError -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}

fun <T> BaseVmDbFragment<*, *>.parseModState(
    resultState: ModResultState<T>,
    onSuccess: (T?) -> Unit,
    onError: ((ModAppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ModResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }

        is ModResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }

        is ModResultState.ModError -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}


/**
 * [UI辅助] 在 Activity 中解析并处理 ModResultStateWithMsg 的状态。
 * 成功回调同时返回 data 和 message。
 */
fun <T> BaseModVmDbActivity<*, *>.parseModStateWithMsg(
    resultState: ModResultStateWithMsg<T>,
    onSuccess: (data: T?, msg: String?) -> Unit,
    onError: ((ModAppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ModResultStateWithMsg.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }

        is ModResultStateWithMsg.Success -> {
            dismissLoading()
            onSuccess(resultState.data, resultState.message)
        }

        is ModResultStateWithMsg.Error -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}

fun <T> BaseVmDbFragment<*, *>.parseModStateWithMsg(
    resultState: ModResultStateWithMsg<T>,
    onSuccess: (data: T?, msg: String?) -> Unit,
    onError: ((ModAppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ModResultStateWithMsg.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }

        is ModResultStateWithMsg.Success -> {
            dismissLoading()
            onSuccess(resultState.data, resultState.message)
        }

        is ModResultStateWithMsg.Error -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}


/**
 * [新增方法] 专为 ModResultStateWithMsg 设计的通用网络请求方法。
 * 它可以在请求成功时，将 data 和 msg 同时返回。
 */
fun <T> BaseViewModel.modRequestWithMsg(
    block: suspend () -> ModApiResponse<T>,
    resultState: MutableLiveData<ModResultStateWithMsg<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return modSafeLaunch(
        block = block,
        onStart = {
            if (isShowDialog) resultState.value = ModResultStateWithMsg.onAppLoading(loadingMessage)
        },
        onSuccess = { response ->
            // 在成功回调中，根据业务码分别处理
            if (response.isSucceed()) {
                // 业务成功，封装 Success 状态，同时传入 data 和 msg
                resultState.value = ModResultStateWithMsg.onAppSuccess(
                    response.getResponseData(),
                    response.getResponseMsg()
                )
            } else {
                // 业务失败，封装 Error 状态
                resultState.value = ModResultStateWithMsg.onAppError(
                    ModAppException(
                        response.getResponseStatus(),
                        response.getResponseMsg(),
                        response.getResponseMsg(),
                        null,
                    )
                )
            }
        },
        onFailure = { exception ->
            // 网络或解析异常，封装 Error 状态
            resultState.value =
                ModResultStateWithMsg.onAppError(ModExceptionHandle.handleException(exception))
        }
    )
}

/**
 * [UI辅助] 在 Fragment 中解析并处理 ResultState 的状态。
 */
fun <T> BaseVmDbFragment<*, *>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T?) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: ((message: String) -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            if (onLoading == null) {
                showLoading(resultState.loadingMessage)
            } else {
                onLoading.invoke(resultState.loadingMessage)
            }
        }

        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }

        is ResultState.Error -> {
            dismissLoading()
            onError?.invoke(resultState.error)
        }
    }
}

// =================================================================================================
// ===                             核心网络请求封装 (兼容旧版 ResultState)                         ===
// =================================================================================================

/**
 * [兼容旧版] 通用的网络请求方法，结合 LiveData 和 ResultState 模式。
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return safeLaunch(
        block = block,
        onStart = {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
        },
        onSuccess = { response ->
            resultState.paresResult(response)
        },
        onFailure = { exception ->
            resultState.paresException(exception)
        }
    )
}

/**
 * [兼容旧版] 回调式的网络请求方法。
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    success: (T?) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return safeLaunch(
        block = { executeResponse(block()) },
        onStart = { if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage) },
        onSuccess = { data ->
            loadingChange.dismissDialog.postValue(false)
            success(data)
        },
        onFailure = { exception ->
            loadingChange.dismissDialog.postValue(false)
            error(ExceptionHandle.handleException(exception))
        }
    )
}

// =================================================================================================
// ===                         新增核心网络请求封装 (适配 ModResultState)                         ===
// =================================================================================================

/**
 * 专为 ModResultState 设计的通用网络请求方法。
 */
fun <T> BaseViewModel.modRequest(
    block: suspend () -> ModApiResponse<T>,
    resultState: MutableLiveData<ModResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return modSafeLaunch(
        block = block,
        onStart = {
            if (isShowDialog) resultState.value = ModResultState.onAppLoading(loadingMessage)
        },
        onSuccess = { response ->
            resultState.paresModResult(response)
        },
        onFailure = { exception ->
            resultState.paresModException(exception)
        }
    )
}

// 您需要找到 modSafeLaunch 并做类似修改
// 这是一个示例，您需要根据您的 modSafeLaunch 实现来调整
suspend fun <T> modAppRequest(
    block: suspend () -> ModApiResponse<T>,
    resultState: MutableLiveData<ModResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
) {
    // 不再返回 Job，不再启动新协程，而是直接在当前协程执行
    try {
        if (isShowDialog) {
            // 注意：这里更新 LiveData 可能需要切换到主线程
            withContext(Dispatchers.Main) {
                resultState.value = ModResultState.onAppLoading(loadingMessage)
            }
        }
        // 直接在调用方的协程上执行 block
        val response = block()
        // 同样，更新 LiveData 可能需要切回主线程
        withContext(Dispatchers.Main) {
            resultState.paresModResult(response)
        }
    } catch (exception: Exception) {
        if (exception !is CancellationException) {
            withContext(Dispatchers.Main) {
                resultState.paresModException(exception)
            }
        } else {
            // 如果是取消异常，则向上抛出
            throw exception
        }
    }
}


/**
 * 专为 ModResultState 设计的回调式网络请求方法。
 */
fun <T> BaseViewModel.modRequestWithCallback(
    block: suspend () -> ModApiResponse<T>,
    success: (T) -> Unit,
    error: (ModAppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return modSafeLaunch(
        block = { executeModResponse(block()) },
        onStart = { if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage) },
        onSuccess = { data ->
            loadingChange.dismissDialog.postValue(false)
            data?.let { success(it) }
                ?: error(ModAppException("err", "请求成功但服务器返回数据为null"))
        },
        onFailure = { exception ->
            loadingChange.dismissDialog.postValue(false)
            error(ModExceptionHandle.handleException(exception))
        }
    )
}


// =================================================================================================
// ===                          核心封装与辅助工具 (私有，请勿直接调用)                            ===
// =================================================================================================

/**
 * [私有核心封装 - 旧版] 驱动 ResultState 系列请求的唯一“引擎”。
 */
private fun <T> BaseViewModel.safeLaunch(
    block: suspend () -> T,
    onStart: () -> Unit,
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit
): Job {
    return viewModelScope.launch {
        try {
            onStart()
            val result = runCatching {
                initializeNetwork()
                block()
            }.getOrThrow()
            onSuccess(result)
        } catch (e: Throwable) {
            e.message?.loge()
            e.printStackTrace()
            onFailure(e)
        }
    }
}

/**
 * [私有核心封装 - 新版] 驱动 ModResultState 系列请求的唯一“引擎”。
 */
private fun <T> BaseViewModel.modSafeLaunch(
    block: suspend () -> T,
    onStart: () -> Unit,
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit
): Job {
    return viewModelScope.launch {
        try {
            onStart()
            val result = runCatching {
                initializeNetwork()
                block()
            }.getOrThrow()
            onSuccess(result)
        } catch (e: Throwable) {
            e.message?.loge()
            e.printStackTrace()
            onFailure(e)
        }
    }
}


/**
 * [私有辅助工具 - 旧版] 校验 BaseResponse 业务码。
 */
private suspend fun <T> executeResponse(response: BaseResponse<T>): T? {
    if (response.isSucceed()) {
        return response.getResponseData()
    } else {
        throw AppException(response.getResponseCode(), response.getResponseMsg())
    }
}

/**
 * [私有辅助工具 - 新版] 校验 ModApiResponse 业务码。
 */
private suspend fun <T> executeModResponse(response: ModApiResponse<T>): T? {
    if (response.isSucceed()) {
        return response.getResponseData()
    } else {
        throw ModAppException(response.getResponseCode().toString(), response.getResponseMsg())
    }
}


// =================================================================================================
// ===                             特定业务封装 (已重构为安全调用)                                 ===
// =================================================================================================

/**
 * [已重构] 异步刷新用户Token。这是一个安全的、非阻塞的请求。
 */
fun BaseViewModel.refreshToken() {
    request(
        block = {
            val map = mutableMapOf<String, Any>()
            map["token"] = MMKVUtil.getJwtRefreshToken() ?: ""
            map["device"] = 21
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                Gson().toJson(map)
            )
            apiService.refreshToken(body)
        },
        success = { data ->
            data?.let {
                MMKVUtil.saveJwtToken(it.jwtToken)
                MMKVUtil.saveJwtRefreshToken(it.jwtRefreshToken)
            }
        },
        error = {
        }
    )
}

/**
 * [已重构] 异步刷新AI Token。
 */
fun BaseViewModel.refreshAiToken() {
    request(
        block = {
            val map = mutableMapOf<String, Any>()
            map["refresh"] = MMKVUtil.getAiRefreshToken() ?: ""
            map["device"] = 21
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                Gson().toJson(map)
            )
            apiService.refreshAiToken(body)
        },
        success = { data ->
            data?.let {
                MMKVUtil.saveAiRefreshToken(it.refresh)
            }
        },
        error = {
        }
    )
}


// =================================================================================================
// ===                                链式网络请求 (已恢复并加固)                                  ===
// =================================================================================================

/**
 * 自定义异常，用于在链式请求中传递原始的失败响应信息。
 */
class ChainedRequestException(val response: BaseResponse<*>) :
    Exception(response.getResponseMsg())

/**
 * 链式请求构造器。
 */
class RequestChain<T> private constructor(
    private val viewModel: BaseViewModel,
    private val block: suspend () -> BaseResponse<T>
) {

    /**
     * 连接下一个网络请求。
     */
    fun <R> then(nextBlock: suspend (T) -> BaseResponse<R>): RequestChain<R> {
        val newBlock: suspend () -> BaseResponse<R> = {
            val previousData = executeResponse(block())
            if (previousData != null) {
                nextBlock(previousData)
            } else {
                throw AppException(-1, "请求成功但返回数据为null，链式请求中断")
            }
        }
        return RequestChain(viewModel, newBlock)
    }

    /**
     * 最终执行整个请求链，并监听最终结果。
     */
    fun launchIn(
        resultState: MutableLiveData<ResultState<T>>,
        isShowDialog: Boolean = false,
        loadingMessage: String = "请求网络中..."
    ): Job {
        // 链式请求的启动，最终也委托给统一的、经过加固的 request 方法
        return viewModel.request(
            block = block,
            resultState = resultState,
            isShowDialog = isShowDialog,
            loadingMessage = loadingMessage
        )
    }

    companion object {
        /**
         * 静态方法，用于创建请求链的第一个环节。
         */
        fun <T> with(
            viewModel: BaseViewModel,
            firstBlock: suspend () -> BaseResponse<T>
        ): RequestChain<T> {
            return RequestChain(viewModel, firstBlock)
        }
    }
}

/**
 * 为 BaseViewModel 提供链式请求的 DSL 入口函数。
 */
fun <T> BaseViewModel.requestChain(
    firstBlock: suspend () -> BaseResponse<T>
): RequestChain<T> {
    return RequestChain.with(this, firstBlock)
}

// =================================================================================================
// ===                          ModResultState 的链式网络请求                                ===
// =================================================================================================


/**
 * 自定义异常，用于在`mod`链式请求中传递原始的失败响应信息。
 */
class ModChainedRequestException(val response: ModApiResponse<*>) :
    Exception(response.getResponseMsg())


/**
 * 专为 ModApiResponse 设计的链式请求构造器。
 */
class ModRequestChain<T> private constructor(
    private val viewModel: BaseViewModel,
    private val block: suspend () -> ModApiResponse<T>
) {

    /**
     * 连接下一个网络请求。
     */
    fun <R> then(nextBlock: suspend (T) -> ModApiResponse<R>): ModRequestChain<R> {
        val newBlock: suspend () -> ModApiResponse<R> = {
            val previousData = executeModResponse(block())
            if (previousData != null) {
                nextBlock(previousData)
            } else {
                throw ModAppException("err", "请求成功但返回数据为null，链式请求中断")
            }
        }
        return ModRequestChain(viewModel, newBlock)
    }

    /**
     * 最终执行整个请求链，并监听最终结果。
     */
    fun launchIn(
        resultState: MutableLiveData<ModResultState<T>>,
        isShowDialog: Boolean = false,
        loadingMessage: String = "请求网络中..."
    ): Job {
        return viewModel.modRequest(
            block = block,
            resultState = resultState,
            isShowDialog = isShowDialog,
            loadingMessage = loadingMessage
        )
    }

    companion object {
        /**
         * 静态方法，用于创建`mod`请求链的第一个环节。
         */
        fun <T> with(
            viewModel: BaseViewModel,
            firstBlock: suspend () -> ModApiResponse<T>
        ): ModRequestChain<T> {
            return ModRequestChain(viewModel, firstBlock)
        }
    }
}

/**
 * 为 BaseViewModel 提供专为 ModResultState 设计的链式请求 DSL 入口函数。
 */
fun <T> BaseViewModel.modRequestChain(
    firstBlock: suspend () -> ModApiResponse<T>
): ModRequestChain<T> {
    return ModRequestChain.with(this, firstBlock)
}



/**************************************************************************************************/

/**
 * 专为实现“可观察的顺序流程”而设计的执行器。
 */
class RequestFlow(private val viewModel: BaseViewModel) {

    /**
     * 执行流程中的一个步骤。
     * 它会像 modRequestWithMsg 一样实时更新 LiveData，
     * 并且在业务成功后，挂起函数才会返回成功的数据。
     * @param block 网络请求的挂起函数。
     * @param resultState 用于接收此步骤实时状态的 LiveData。
     * @return T 业务成功时返回的数据。如果失败，则抛出异常中断整个流程。
     */
    suspend fun <T> step(
        block: suspend () -> ModApiResponse<T>,
        resultState: MutableLiveData<ModResultStateWithMsg<T>>,
        isShowDialog: Boolean = false,
        loadingMessage: String = "请求网络中..."
    ): T {
        // 使用 suspendCancellableCoroutine 将回调模式的 modSafeLaunch 桥接到挂起函数
        return suspendCancellableCoroutine { continuation ->
            viewModel.modSafeLaunch(
                block = block,
                onStart = {
                    if (isShowDialog) resultState.postValue(ModResultStateWithMsg.onAppLoading(loadingMessage))
                },
                onSuccess = { response ->
                    if (response.isSucceed()) {
                        val data = response.getResponseData()
                        // 业务成功
                        resultState.postValue(ModResultStateWithMsg.onAppSuccess(
                            data,
                            response.getResponseMsg()
                        ))

                        if (data == null) {
                            continuation.resumeWithException(
                                ModAppException("err_null_data", "请求成功但返回数据为null，流程中断")
                            )
                        } else {
                            continuation.resume(data)
                        }
                    } else {
                        // 业务失败
                        val appException = ModAppException(
                            response.getResponseStatus(),
                            response.getResponseMsg() ?: "Unknown error"
                        )
                        resultState.postValue(ModResultStateWithMsg.onAppError(appException))
                        // 以异常方式恢复协程，中断整个流程
                        continuation.resumeWithException(appException)
                    }
                },
                onFailure = { exception ->
                    // 网络或解析异常
                    val appException = ModExceptionHandle.handleException(exception)
                    resultState.postValue(ModResultStateWithMsg.onAppError(appException))
                    // 以异常方式恢复协程，中断整个流程
                    continuation.resumeWithException(appException)
                }
            )
            continuation.invokeOnCancellation {
            }
        }
    }
}

/**
 * 为 BaseViewModel 提供流程的入口函数
 */
fun BaseViewModel.requestFlow(
    action: suspend RequestFlow.() -> Unit
) {
    val flow = RequestFlow(this)
    this.viewModelScope.launch {
        try {
            flow.action()
        } catch (e: Exception) {
            // 可以在这里统一处理流程中任何步骤抛出的异常
            Logs.e("RequestFlow", "Flow was interrupted by an exception: ${e.message}")
        }
    }
}





fun BaseViewModel.getOAID(
    application: Application,
    onSuccess: (String) -> Unit,
): Job {
    return viewModelScope.launch {
        try {
            val oaid = getOAIDWithCoroutines(application)
            onSuccess(oaid)
        } catch (e: Throwable) {
            e.message?.loge()
            e.printStackTrace()
        }
    }
}


//val updateCountResult = MutableLiveData<ModResultState<UpdateResult>>()
//fun fetchUserOrderAndUpdateProduct() {
//    modRequestChain {
//        apiService.getUserInfo()
//    }.then { userInfo ->
//        Log.d("Chain", "第一步成功，获取到用户: ${userInfo.name}")
//        apiService.getOrderList(userInfo.id)
//    }.then { orderList ->
//        Log.d("Chain", "第二步成功，获取到订单数量: ${orderList.size}")
//        val latestOrderId = orderList.first().id
//        apiService.getOrderDetail(latestOrderId)
//    }.then { orderDetail ->
//        Log.d("Chain", "第三步成功，获取到订单详情，商品是: ${orderDetail.productName}")
//        val productId = orderDetail.productId
//        apiService.updateProductViewCount(productId)
//    }.launchIn(updateCountResult, isShowDialog = true) // 启动整个链条
//}
