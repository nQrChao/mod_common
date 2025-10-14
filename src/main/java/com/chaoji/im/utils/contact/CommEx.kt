package com.chaoji.im.utils.contact

import java.io.Serializable
import java.util.Objects

/**
 * 常用扩展字段
 */
open class CommEx : Serializable {
    @JvmField
    var key: String? = null //Id
    @JvmField
    var isSticky = false //是否是Sticky
    @JvmField
    var sortLetter: String? = null //显示数据拼音的首字母
    @JvmField
    var isSelect = false //是否被选中
    @JvmField
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



