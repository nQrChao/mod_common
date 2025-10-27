package com.box.base.state
import androidx.lifecycle.MutableLiveData
import com.box.base.network.BaseResponse
import com.box.mod.modnetwork.ModAppException
import com.box.mod.modnetwork.ModExceptionHandle

sealed class ModResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T?): ModResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ModResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: ModAppException): ModResultState<T> = ModError(error)
    }

    data class Loading(val loadingMessage: String) : ModResultState<Nothing>()
    data class Success<out T>(val data: T?) : ModResultState<T>()
    data class ModError(val error: ModAppException) : ModResultState<Nothing>()
}

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ModResultState<T>>.paresResult(result: BaseResponse<T>) {
    value = when {
        result.isSucceed() -> {
            ModResultState.onAppSuccess(result.getResponseData())
        }
        else -> {
            ModResultState.onAppError(ModAppException(-1,"",result.getResponseMsg()))
        }
    }
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ModResultState<T>>.paresResult(result: T) {
    value = ModResultState.onAppSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ModResultState<T>>.paresException(e: Throwable) {
    this.value = ModResultState.onAppError(ModExceptionHandle.handleException(e))
}


/**
 * 与 ModResultState 的主要区别在于，Success 状态同时携带了 data 和 message。
 */
sealed class ModResultStateWithMsg<out T> {
    companion object {
        fun <T> onAppSuccess(data: T?, message: String?): ModResultStateWithMsg<T> = Success(data, message)
        fun <T> onAppLoading(loadingMessage: String): ModResultStateWithMsg<T> = Loading(loadingMessage)
        fun <T> onAppError(error: ModAppException): ModResultStateWithMsg<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ModResultStateWithMsg<Nothing>()
    data class Success<out T>(val data: T?, val message: String?) : ModResultStateWithMsg<T>()
    data class Error(val error: ModAppException) : ModResultStateWithMsg<Nothing>()
}
