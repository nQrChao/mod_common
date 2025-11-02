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