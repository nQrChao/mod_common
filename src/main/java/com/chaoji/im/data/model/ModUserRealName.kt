package com.chaoji.im.data.model

import com.chaoji.other.blankj.utilcode.util.StringUtils
import java.io.Serializable

data class ModUserRealName(
    val hascert: String = "",
    val has18: String = "",

) : Serializable {

    fun isRealName():Boolean{
        return !StringUtils.isEmpty(hascert)
    }

    fun isRealName18():Boolean{
        return !StringUtils.isEmpty(has18)
    }


}

