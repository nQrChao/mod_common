package com.chaoji.im.data.model

import com.chaoji.base.network.BaseResponse


data class ApiResponse<T>(val errCode: Int?, val errDlt: String?,val errMsg:String?, val data: T?) : BaseResponse<T>() {

    override fun isSucceed() :Boolean{
       return errCode==0
    }

    override fun getResponseCode() = errCode?:0

    override fun getResponseData() = data

    override fun getResponseMsg() = errDlt?:""

}