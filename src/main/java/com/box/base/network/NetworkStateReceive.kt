package com.box.base.network

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 现代化改造：此类不再是 BroadcastReceiver，
 * 而是继承自 ConnectivityManager.NetworkCallback 来监听网络状态变化。
 */
class NetworkStateReceive : ConnectivityManager.NetworkCallback() {
    // 当网络连接可用时（或网络类型变化时）调用
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        // 逻辑迁移：相当于之前 isNetworkAvailable(context) 为 true 的情况
        postState(true) // 发送有网的状态
    }
    // 当网络连接断开时调用
    override fun onLost(network: Network) {
        super.onLost(network)
        // 逻辑迁移：相当于之前 isNetworkAvailable(context) 为 false 的情况
        postState(false) // 发送没网的状态
    }

    /**
     * 将原有的状态更新逻辑封装成一个方法
     * @param isSuccess 网络是否可用
     */
    private fun postState(isSuccess: Boolean) {
        // 获取当前状态
        val currentState = NetworkStateManager.instance.mNetworkStateCallback.value

        if (currentState?.isSuccess == isSuccess) {
            // 如果当前状态和要发送的状态相同，则不重复发送，防止重复通知
            return
        }
        // 发送新的网络状态
        NetworkStateManager.instance.mNetworkStateCallback.postValue(NetState(isSuccess = isSuccess))
    }
}