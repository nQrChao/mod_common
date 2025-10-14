package com.chaoji.im.data.model

import java.io.Serializable

data class ModGameFuLiInfo(
    var coupon_amount: String = "",
    var coupon_count: String = "",
    var gameid: String = "",
    var rebate_flash_begin: Int = 0,
    var rebate_flash_end: Int = 0,
    var user_already_commented: Int = 0,
    var max_rate: Int = 0,
    var vip_news: VipNews,
    var lsb_card_info: ModLiBaoBean,
    var cardlist: MutableList<ModLiBaoBean> = mutableListOf(),
    var activity: MutableList<VipNews> = mutableListOf(),
    var coupon_list: MutableList<CouponList> = mutableListOf(),
    val game_labels: MutableList<ModGameLabelBean> = mutableListOf(),
) : Serializable {
    data class ActivityData(
        var vid: String = "",
        var title: String = "",
        var title2: String = "",
        var endtime: String = "",
        var typeid: String = "",
        var begintime: String = "",
        var fabutime: String = "",
        var laiyuan: String = "",
        var is_newest: String = "",
        var rebate_apply_type: String = "",
        var url: String = "",
        var news_status: String = "",
        var rebate_url: String = "",
    ) : Serializable

    data class VipNews(
        var vid: String = "",
        var title: String = "",
        var title2: String = "",
        var endtime: String = "",
        var typeid: String = "",
        var begintime: String = "",
        var fabutime: String = "",
        var laiyuan: String = "",
        var is_newest: String = "",
        var rebate_apply_type: String = "",
        var url: String = "",
        var news_status: String = "",
        var rebate_url: String = "",
    ) : Serializable

    data class CouponList(
        var id: String = "",
        var amount: String = "",
        var condition: String = "",
        var gameid: String = "",
        var status: String = "",
        var sign: String = "",
        var m_price: String = "",
        var coupon_type: String = "",
        var range: String = "",
        var user_get_count: String = "",
        var frequency: String = "",
        var can_get_label: String = "",
        var expiry_label: String = "",
    ) : Serializable{
        val displayName: String
            get() {
                // 先判断是否为 null
                if (range == null) return ""
                // 找到第一个'（'的位置
                val index = range.indexOf('（')
                // 如果找到了，就截取前面的部分；否则返回原字符串
                return if (index != -1) range.substring(0, index) else range
            }
    }

}
