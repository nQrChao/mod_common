package com.box.common.ui.xpop

import androidx.lifecycle.MutableLiveData
import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.ext.request
import com.box.base.state.ResultState
import com.box.common.data.model.AIChat
import com.box.common.data.model.AiMessage
import com.box.common.network.apiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class XPopupDrawerAiListModel : BaseViewModel() {
    var chatMessage = MutableLiveData<AIChat>()
    var chatMessageId = ""
    var aiChatList = MutableLiveData<MutableList<AIChat>>()

    var aiChatDelResult = MutableLiveData<ResultState<Any?>>()
    var aiChatNameResult = MutableLiveData<ResultState<Any?>>()
    var aiChatDelAllResult = MutableLiveData<ResultState<Any?>>()
    var aiChatCreateResult = MutableLiveData<ResultState<AIChat>>()
    var aiChatListResult = MutableLiveData<ResultState<MutableList<AIChat>>>()
    var aiChatMessageResult = MutableLiveData<ResultState<MutableList<AiMessage>>>()

    fun chatList() {
        request({
            apiService.aiChatList()
        }, aiChatListResult)
    }

    fun chatName(id: String, title: String) {
        request({
            val map = mutableMapOf<String, Any>()
            map["tittle"] = title
            val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), Gson().toJson(map))
            apiService.aiChatName(id, body)
        }, aiChatNameResult)
    }

    fun chatCreate(title: String) {
        val map = mutableMapOf<String, Any>()
        map["tittle"] = title
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), Gson().toJson(map))
        request({ apiService.aiChatCreate(body) }, aiChatCreateResult)
    }

    fun chatDel(id: String) {
        request({
            apiService.aiChatDel(id)
        }, aiChatDelResult)
    }

    fun chatDelAll() {
        request({
            apiService.aiChatDelAll()
        }, aiChatDelAllResult)
    }

    fun chatMessage(id: String) {
        chatMessageId = id
        request({
            apiService.aiChatMessage(id)
        }, aiChatMessageResult)
    }

}