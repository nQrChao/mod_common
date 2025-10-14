package com.box.common.data.model

import java.io.Serializable

class AppletsDataMarketJson(
    val xieyitanchuang_biaoti: String = "",
    val xieyitanchuang_neirong: String = "",
    val xieyitanchuang_url_fuwu: String = "",
    val xieyitanchuang_url_yinsi: String = "",
    val xieyitanchuang_url_fcm: String = "",
    val xieyitanchuang_url_zhuxiao: String = "",
    val xieyitanchuang_url_guanyu: String = "",
    val xieyitanchuang_url_geren: String = "",
    val xieyitanchuang_url_disanfang: String = "",
    val qq: String = "",
    val wechat_url: String = "",
    val app_beianhao: String = "",
    val app_beianhao_url: String = "",
    val AUTH_LOGIN_SIGN_INFO: String = "",
    val list_data: MutableList<AppletsInfo> = mutableListOf()
) : Serializable