package com.chaoji.im.data.model

import java.util.*

open class CommEx :java.io.Serializable{
    var key //Id
            : String? = null
    var isSticky = false //是否是Sticky

    var sortLetter //显示数据拼音的首字母
            : String? = null

    var isSelect = false //是否被选中

    var isEnabled = true //是否可点击

    override fun equals(obj: Any?): Boolean {
        if (null != obj) {
            if (hashCode() ==
                obj.hashCode()
            ) return true
        }
        return super.equals(obj)
    }

    override fun hashCode(): Int {
        return Objects.hash(key)
    }
}