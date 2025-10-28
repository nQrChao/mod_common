package com.box.common.data.model

import java.io.Serializable

data class ModGameListBean(
    var id : String = "",
    var name : String = "",
    var url : String = "",
    var tag : String = "",
    var createTime : String = "",
    ) : Serializable