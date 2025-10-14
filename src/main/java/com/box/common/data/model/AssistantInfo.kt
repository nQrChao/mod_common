package com.box.common.data.model

import java.io.Serializable


class AssistantInfo : Serializable {
     var id: Int = 0
     var parentid: Int =0
     var stype: Int = 0
     var title: String = ""
     var info: String = ""
     var prompt: String = ""
     var gptpormpt: String = ""
     var icon: String = ""
     var color: String = ""
     var formcontext: String = ""
 }
