package com.box.common.impl

class CommonSelectImpl(var leftImage:Int=0,var title:String,var itemId:String) :CommonSelect{
    override fun getLeftImageRes(): Int =leftImage

    override fun getText(): String =title

    override fun getId(): String =itemId
}