package com.chaoji.im.data.model

import android.text.SpannableStringBuilder
import java.io.Serializable

class MsgExpand : Serializable {
    var isChoice = false

    //此item 应该显示时间
    var isShowTime = false

    //地址信息（发送位置才有）
    var locationDesc: LocationDesc? = null

    //at消息（at别人才有）
    var atMsgInfo: AtMsgInfo? = null

    //用于在消息输入框监听删除键时 判断删除对应@的人
    var spanHashCode = 0

    //富文本（at 消息、表情）
    var sequence: SpannableStringBuilder? = null

    //群公告、消息通知
    var notificationMsg: NotificationMsg? = null

    //普通通知 富文本
    var tips: CharSequence? = null

    //阅后即焚倒计时
    var readVanishNum = 0

    // ---呼叫记录---
    var callHistory: CallHistory? = null

    // 呼叫时长
    var callDuration: String? = null

    //会议邀请信息
    var meetingInfo: MeetingInfo? = null

    //自定义表情
    var customEmoji: CustomEmojiEntity? = null

    //发送消息进度
    var sendProgress: Long = 0

}