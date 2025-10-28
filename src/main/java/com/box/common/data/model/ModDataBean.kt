package com.box.common.data.model

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
) : Serializable{
    //getModGameList [{"id":0,"name":"","url":"","tag":"","createTime":""}]
    //getNewsDetailById {"id":0,"content":""}
    //getNewsList [{"id":0,"title":"","image":"","description":"","views":,"deleteTime":,"createTime":}]
    //getRandomName {"id":1,"type":"1","name":"","createTime":"","length":3}
    //getRoleType [{"dictLabel":"","dictValue":"1"}]

}