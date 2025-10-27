package com.box.mod.modnetwork


enum class ModError(private val state: Int, private val msg: String) {

    /**
     * 未知错误
     */
    ERR(-1, ""),
    /**
     * 未知错误
     */
    UNKNOWN(-2, "请求失败，请稍后再试"),
    /**
     * 解析错误
     */
    PARSE_ERROR(-3, "解析错误，请稍后再试"),
    /**
     * 网络错误
     */
    NETWORK_ERROR(500, "网络连接错误，请稍后重试"),

    /**
     * 证书出错
     */
    SSL_ERROR(444, "证书出错，请稍后再试"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(555, "网络连接超时，请稍后重试");


    fun getValue(): String {
        return msg
    }

    fun getState(): Int {
        return state
    }

}