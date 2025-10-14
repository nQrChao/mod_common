package com.chaoji.im.network

import com.chaoji.base.network.BaseResponse

data class ApiResponse<T>(val status: String?, val msg: String?, val data: T?) : BaseResponse<T>() {
    override fun isSucceed(): Boolean {
        return status == "ok"
    }
    override fun getResponseCode():Int{
        return if (status == "ok") 200 else 0
    }

    override fun getResponseData() = data

    override fun getResponseMsg() = msg ?: ""

}