package com.box.common.data.model

import java.io.Serializable

data class ModSplashVo(
    var authLogin: ModUserInfoBean? = null,
    var appInit: ModInitData? = null,
    var splashBeanVo: ModSplashBean? = null,
    var appStyleVo: ModAppStyle? = null
) : Serializable {
    data class ModSplashBean(
        val pic: String? = null,
        val type: String? = null,
        val page_type: String? = null,
        val param: ModAppJumpInfoBean? = null
    ) {
    }
}