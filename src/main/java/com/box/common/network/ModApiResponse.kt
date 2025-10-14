package com.box.common.network

import com.box.base.network.BaseResponse

data class ModApiResponse<T>(val msg: String?, val data: T?, val state: String?) :
    BaseResponse<T>() {
    override fun isSucceed(): Boolean {
        return state == "ok"
    }

    override fun getResponseCode(): Int {
        return if (state == "ok") 200 else 0
    }

    override fun getResponseData() = data
    override fun getResponseMsg() = msg ?: ""
    fun getResponseStatus(): String = state ?: ""

}