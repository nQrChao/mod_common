package com.chaoji.im.event

import androidx.lifecycle.MutableLiveData
import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.im.data.model.AIChat
import com.chaoji.im.data.model.ImError
import com.chaoji.im.data.model.AppletsInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class EventViewModel : BaseViewModel() {
    val isLogin = UnPeekLiveData<Boolean>()
    var splashResult = MutableLiveData<Boolean>()
    val startMJ = UnPeekLiveData<Boolean>()
    val startGame = UnPeekLiveData<Boolean>()
    val jumpGame = UnPeekLiveData<String>()
    val fuliBi = UnPeekLiveData<String>()
    val main = UnPeekLiveData<String>()
    val scrollToTabGameList = UnPeekLiveData<Boolean>()
    val goTest = UnPeekLiveData<Int>()
    //val checkUser: UnPeekLiveData<MutableList<CommonUserInfo>> = UnPeekLiveData.Builder<MutableList<CommonUserInfo>>().setAllowNullValue(false).create()
    //val showMsgConversation = UnPeekLiveData<MutableList<MsgConversation>>()
    val showLogView = UnPeekLiveData<Boolean>()
    val showInfoView = UnPeekLiveData<Boolean>()

    val updateHideConversation = UnPeekLiveData<String>()

    val hideConversation = UnPeekLiveData<String>()

    val resetConversation = UnPeekLiveData<String>()

    val delConversation = UnPeekLiveData<String>()

    val updateConversation = UnPeekLiveData<String>()


    val getAiChatCount = UnPeekLiveData<String>()

    val updateAiChatFragment = UnPeekLiveData<AIChat>()

    val deleteAllAiChat = UnPeekLiveData<String>()

    val deleteAllMsg = UnPeekLiveData<String>()

    val mainTouchEvent = UnPeekLiveData<Boolean>()

    val messageLoadingState = UnPeekLiveData<Boolean>()

    //connListener

    val onConnectFailed = UnPeekLiveData<ImError>()

    val onConnectSuccess = UnPeekLiveData<Boolean>()

    val onConnecting = UnPeekLiveData<Boolean>()

    //踢下线
    val onKickedOffline = UnPeekLiveData<Boolean>()

    //用户信息过期
    val onUserTokenExpired = UnPeekLiveData<Boolean>()

    val setMainCurrentItem = UnPeekLiveData<Int>()

    val setDefaultGameId = UnPeekLiveData<Int>()

    val setNavigation2Info = UnPeekLiveData<AppletsInfo>()
    val setNavigation2InfoIndex = UnPeekLiveData<Int>()

    val showMainCurrentItem = UnPeekLiveData<Boolean>()
    //GroupListeners
    val onGroupMemberClassUpdate = UnPeekLiveData<String>()

    val onGroupClassMoved = UnPeekLiveData<String>()

    val onFriendGroupMoved = UnPeekLiveData<Boolean>()

    val onGroupQuit = UnPeekLiveData<Boolean>()

    val onGroupClassUpdate = UnPeekLiveData<Boolean>()

    val onSyncServerFailed = UnPeekLiveData<Boolean>()
    val onSyncServerFinish = UnPeekLiveData<Boolean>()
    val onSyncServerStart = UnPeekLiveData<Boolean>()
    val onTotalUnreadMessageCountChanged = UnPeekLiveData<Int>()

    val toFragment = UnPeekLiveData<String>()

    val toShangJiaView = UnPeekLiveData<String>()
    val clientType = UnPeekLiveData<String>()
    val productType = UnPeekLiveData<String>()

    val fragment1Tab = UnPeekLiveData<Int>()


}