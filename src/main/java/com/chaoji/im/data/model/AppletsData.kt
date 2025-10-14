package com.chaoji.im.data.model

import java.io.Serializable

class AppletsData(
    var id: String = "",
    var title: String = "",
    var title2: String = "",
    var pic: String = "",
    var pic2: String = "",
    var redirect: String = "",
    var marketjson: AppletsDataMarketJson = AppletsDataMarketJson()
) : Serializable