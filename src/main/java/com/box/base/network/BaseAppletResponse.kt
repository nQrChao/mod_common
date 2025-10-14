package com.box.base.network

import com.box.common.data.model.AppletsMeta

abstract class BaseAppletResponse<T> {

    abstract fun isSucceed(): Boolean

    abstract fun getResponseData(): T?

    abstract fun getResponseCode(): Int

    abstract fun getResponseStatus(): Int

    abstract fun getResponseToken(): String

    abstract fun getResponseMeta(): AppletsMeta

    abstract fun getResponseMsg(): String

}