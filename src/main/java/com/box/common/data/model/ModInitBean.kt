package com.box.common.data.model

import java.io.Serializable

data class ModInitBean(
   var id : String = "",
   var appName : String = "",
   var packageName : String = "",
   var signMd5 : String = "",
   var previewPic : String = "",
   var aliAuthLoginSign : String = "",
   var windowTitle : String = "",
   var windowContent : String = "",
   var privacyPolicyLink : String = "",
   var userAgreementLink : String = "",
   var userLogoutLink : String = "",
   var expandText1 : String = "",
   var expandText2 : String = "",
   var expandText3 : String = "",
   var expandText4 : String = "",
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