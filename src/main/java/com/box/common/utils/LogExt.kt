package com.box.common.utils

import com.box.other.blankj.utilcode.util.Logs

fun Any.logd(message: String) {
    Logs.d(this.javaClass.simpleName, message)
}

fun Any.loge(message: String) {
    Logs.e(this.javaClass.simpleName, message)
}

fun Any.logi(message: String) {
    Logs.i(this.javaClass.simpleName, message)
}
fun Any.logw(message: String) {
    Logs.w(this.javaClass.simpleName, message)
}

fun Any.loga(message: String) {
    Logs.a(this.javaClass.simpleName, message)
}