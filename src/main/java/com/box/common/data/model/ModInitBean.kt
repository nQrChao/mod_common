package com.box.common.data.model

import java.io.Serializable

data class ModInitBean(
   var id : String = "",
   var name : String = "",
   var packageName : String = "",
   var type : String = "",
   var status : String = "",
   var checkState : String = "",
   var time : String = "",
   var timeRanges : String = "",
   var url : String = "",
   var timeStatus : String = "",
   var windowTitle : String = "",
   var windowContent : String = "",
   var privacyPolicyLink : String = "",
   var userAgreementLink : String = "",
   var userLogoutLink : String = "",
   var number : String = "",
   var numLink : String = "",
   var reset : String = "",
   var expandText5 : String = "",
   var expandText6 : String = "",
   var expandText7 : String = "",
   var expandText8 : String = "",
   var expandText9 : String = "",
   var expandList1 : MutableList<ModInitBean> = mutableListOf(),


   ) : Serializable {
    data class ListData(
        val title: String = "",
    )

}