package com.chaoji.base.network

abstract class BaseResponse<T> {

    abstract fun isSucceed(): Boolean

    abstract fun getResponseData(): T?

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}