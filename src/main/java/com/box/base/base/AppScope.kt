package com.box.base.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * 创建一个 Kotlin 单例对象来持有应用级的协程作用域。
 * 这样做的好处是：
 * 1. Coroutine 相关代码都留在 Kotlin 世界里，更符合语言特性。
 * 2. 可以从项目的任何地方（Java 或 Kotlin）轻松、安全地访问它。
 * 3. 对你现有的 App.java 文件零侵入。
 */
object AppScope {
    // SupervisorJob() 确保一个任务失败不会取消其他任务。
    // Dispatchers.IO 适合执行网络请求等耗时操作。
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}