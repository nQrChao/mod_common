package com.box.common.data.model

import java.io.Serializable

data class ModValuationCommit(
    var checkState : String = "",
    var checkTime : String = "",
    var createTime : String = "",
    var desc : String = "",
    var fileNames: MutableList<String> = mutableListOf(),
    var froms : MutableList<ModValuationCommitBean> = mutableListOf(),
    var id : String = "",
    var money : String = "",
    var userId : String = "",
) :Serializable {
}

