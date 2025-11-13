package com.box.common.data.model

import java.io.Serializable

data class ModValuationCommitBean(
    var commitId: Int = 0,
    var content : String = "",
    var gameId : Int = 0,
    var hint : String = "",
    var id: Int = 0,
    var required: Int = 0,
    var title: String = "",
) :Serializable {
}

