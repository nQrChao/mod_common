package com.box.common.data.model

import com.box.com.R
import java.io.Serializable

data class ModDataBean(
    var id: Int = 0 ,
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var views: String = "",
    var deleteTime: String = "",
    var name: String = "",
    var url: String = "",
    var type: String = "",
    var length: String = "",
    var tag: String = "",
    var content: String = "",
    var createTime: String = "",
    var dictLabel: String = "",
    var dictValue: String = "",
    //估价系统
    var checkTime: String = "",
    var checkState: Int = 0,
    var characteristic: String = "",
    var userId: String = "",
    var money: String = "",
    var gameName: String = "",
    var gameId: String = "",
    var checkNum: String = "",
    var froms: String = "",
    var fileNames: String = "",
    //系统消息
    var createBy: String = "",
    var updateBy: String = "",
    var updateTime: String = "",
    var remark: String = "",
    var noticeId: String = "",
    var noticeTitle: String = "",
    var noticeType: String = "",
    var noticeContent: String = "",
    var status: String = "",
    //自定义数据
    var isSelect: Boolean = false,
    var isShouCang: Boolean = false,
    var rank: Int = 0,

) : Serializable{
    //getModGameList [{"id":0,"name":"","url":"","tag":"","createTime":""}]
    //getNewsDetailById {"id":0,"content":""}
    //getNewsList [{"id":0,"title":"","image":"","description":"","views":,"deleteTime":,"createTime":}]
    //getRandomName {"id":1,"type":"1","name":"","createTime":"","length":3}
    //getRoleType [{"dictLabel":"","dictValue":"1"}]

    fun getRankText():String{
        return rank.toString()
    }
    fun getRankIcon(): Int {
        return when (rank) {
            1 -> R.drawable.mod_rank_top1
            2 -> R.drawable.mod_rank_top2
            3 -> R.drawable.mod_rank_top3
            else ->
                android.R.color.transparent
        }
    }


}