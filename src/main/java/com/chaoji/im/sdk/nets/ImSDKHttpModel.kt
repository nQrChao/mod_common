package com.chaoji.im.sdk.nets

import androidx.lifecycle.LifecycleOwner
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ImSDKHttpModel private constructor() {
    object InstanceHelper {
        val httpModel = ImSDKHttpModel()
    }

    companion object {
        fun getInstance() = InstanceHelper.httpModel
    }

    fun tLogin(
        lifecycleOwner: LifecycleOwner?,
        failure: (e: Throwable) -> Unit
    ) {

        val mBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val requestBody: RequestBody = mBuilder
//            .addFormDataPart("agentId", loginConfig.agentId)
            .build()

    }

}