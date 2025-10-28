package com.box.common.network

import com.box.base.network.BaseResponse

data class ApiResponse<T>(val status: Int, val msg: String?, val data: T?) : BaseResponse<T>() {
    override fun isSucceed(): Boolean {
        return status == 200
    }
    override fun getResponseCode():Int{
        return status
    }

    override fun getResponseData() = data

    override fun getResponseMsg() = msg ?: ""

}