package com.chaoji.base.network

import com.chaoji.base.callback.livedata.event.EventLiveData


class NetworkStateManager private constructor() {

    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}