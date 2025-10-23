package com.box.common.utils

import com.box.other.blankj.utilcode.util.Logs

fun Any.logsD(message: String) {
    Logs.d(this.javaClass.simpleName, message)
}

fun Any.logsE(message: String) {
    Logs.e(this.javaClass.simpleName, message)
}

fun Any.logsI(message: String) {
    Logs.i(this.javaClass.simpleName, message)
}
fun Any.logsW(message: String) {
    Logs.w(this.javaClass.simpleName, message)
}

fun Any.logsA(message: String) {
    Logs.a(this.javaClass.simpleName, message)
}