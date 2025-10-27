package com.box.common.network

import com.box.base.network.BaseResponse

data class ModApiResponse<T>(val msg: String?, val data: T?, val code: Int) :
    BaseResponse<T>() {
    override fun isSucceed(): Boolean {
        return code == 200
    }

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseData() = data
    override fun getResponseMsg() = msg ?: ""
    fun getResponseStatus(): Int = code

}