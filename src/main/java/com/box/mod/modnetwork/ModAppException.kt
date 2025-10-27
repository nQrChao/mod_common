package com.box.mod.modnetwork


class ModAppException : Exception {
    var msg: String //错误消息
    var state: Int //错误码
    var errorLog: String? //错误日志
    var throwable: Throwable? = null

    constructor(state: Int?, msg: String?, errorLog: String = "", throwable: Throwable? = null) : super(msg) {
        this.msg = msg ?: "请求失败，请稍后再试"
        this.state = state ?: -1
        this.errorLog = errorLog
        this.throwable = throwable
    }


    constructor(error: ModError, e: Throwable?) {
        state = error.getState()
        msg = error.getValue()
        errorLog = e?.message
        throwable = e
    }
}