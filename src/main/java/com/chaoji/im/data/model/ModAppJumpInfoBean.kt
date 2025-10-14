package com.chaoji.im.data.model

data class ModAppJumpInfoBean(
     // 使用 override 关键字来重写父类的属性
     override val page_type: String ="",
     override val param: ParamBean?=null,
     // AppJumpInfoBean 自身的新属性
     var pic: String? = null
 ) : ModAppBaseJumpInfoBean(page_type, param)
