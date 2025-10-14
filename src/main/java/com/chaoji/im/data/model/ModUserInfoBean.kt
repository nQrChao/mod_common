package com.chaoji.im.data.model

import java.io.Serializable

data class ModUserInfoBean(
    val uid: String = "",
    val username: String = "",
    val is_special: Int = 0,
    var token: String = "",
    val auth: String = "",
    val is_regular: Int = 0,
    val mobile: String = "",
    val tgid: String = "",
    val pingtaibi: Float = 0f,
    val integral: Int = 0,
    val real_name: String = "",
    val idcard: String = "",
    val invite_type: Int = 0,
    /**
     * 平台币中的人民币部分
     */
    val ptb_rmb: Float = 0f,
    /**
     * 平台币中的非人民币部分
     */
    val ptb_dc: Float = 0f,
    /**
     * 回收时是否需要验证手机号
     */
    val is_oversea_mobile: Boolean = false,
    var password: String = "",
    var login_account: String = "",
    val can_bind_password: Boolean = false,
    /**
     * 当前登录类型 register注册 login登录
     */
    val act: String = "",
    /**
     * 是否上报注册行为 1上报
     */
    val elevate: Int = 0,
    val recall_pop: Int = 0,

    /*****2019.03.05 新增字段**************************************************************************************************/
    /**
     * 用户昵称
     */
    val user_nickname: String = "",
    /**
     * 用户头像
     */
    val user_icon: String = "",
    /**
     * 用户活跃数
     */
    val act_num: Int = 0,
    /**
     * 用户等级,例如 1,2,3…
     */
    val user_level: Int = 0,
    val vip_member: VipMember? = null,
    val vip_info: VipInfoVo? = null,
    val super_user: SuperUser? = null,
    val money_card: MoneyCard? = null,
    val adult: String = ""
) : Serializable {
    data class VipMember(
        val vip_member_status: Int = 0,
        val vip_member_expire: Int = 0,
        val received: Int = 0,
    ) : Serializable

    data class SuperUser(
        val status: String = "",
        val sign: String = "",
    ) : Serializable

    data class MoneyCard(
        val status: String = "",
        val get_reward: String = "",
    ) : Serializable

    data class VipInfoVo(
        /**
         * 用户VIP等级
         */
        val vip_level: Int = 0,
        /**
         * 用户Vip成长值
         */
        val vip_score: Int = 0,
        /**
         * 用户vip下一等级
         */
        val next_level: Int = 0,
        /**
         * 用户vip下一等级所需要的总成长值
         */
        val next_level_score: Int = 0,
        /**
         * 用户当前vip等级所需的成长值
         */
        val current_level_score: Int = 0,
        /**
         * 当值为1时表示有Vip特权礼券可领; 当值为0是表示没有礼券可领取
         */
        val has_coupon: List<String> = mutableListOf()

    ) : Serializable {

        /**
         * 下一等级所需成长值
         */
        val scoreForNextLevel: Int
            get() = next_level_score - vip_score

        /**
         * 下一等级所需成长值所需百分比
         */
        val percentageScoreForNextLevel: Int
            get() = if (next_level_score - current_level_score == 0) {
                100
            } else {
                (vip_score - current_level_score) * 100 / (next_level_score - current_level_score)
            }
    }
}

