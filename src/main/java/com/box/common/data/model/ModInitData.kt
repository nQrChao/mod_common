package com.box.common.data.model


import java.io.Serializable


/**
 * 共用的viewModel
 */
class ModInitData  (
    val frame: List<Int>? = mutableListOf(),
    val theme: ModAppStyle? = null,
    val invite_type: Int = 0,
    val hide_community: Int = 0,
    val wx_control: Int = 0,
    val wxPay_packagenames: List<String>? = null,
    val toutiao_report_amount_limit: Int = 0,
    val reyun_gonghui_tgids: List<String>? = null,
    val toutiao_plug: ToutiaoPlugVo? = null,
    val menu: ModAppMenuBean? = null,
    val profile_setting: ProfileSettingVo? = null,
    var hide_five_figure: Int = 0,
    var cloud_status: Int = 0,
    var pop_gameid: String? = null,
    var show_tip: Int = 0

): Serializable{
    data class ToutiaoPlugVo(
        val toutiao_tgids: List<String>? = null
    )

    data class ProfileSettingVo(
        var web_switch: Int = 0,
        var web_url: String? = null
    )
}