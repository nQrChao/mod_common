package com.box.mod.callback
object CallbackManager {
    // 定义一个可空的变量来持有我们的回调函数
    var jumpCallback: ((String) -> Unit)? = null
}