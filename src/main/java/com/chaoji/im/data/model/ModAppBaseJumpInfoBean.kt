package com.chaoji.im.data.model

import java.io.Serializable

open class ModAppBaseJumpInfoBean(
    open val page_type: String = "",
    open val param: ParamBean? = null
) : Serializable {
    data class ParamBean(
        var gameid: Int = 0,
        var game_type: Int = 0,
        var newsid: String? = null,
        var target_url: String? = null,
        var game_list_id: String? = null,
        var gid: String? = null,
        var container_id: String? = null,
        val share_title: String? = null,
        val share_text: String? = null,
        val share_target_url: String? = null,
        val share_image: String? = null,
        val video_param: ModVideoParam? = null,

        val trial_list: Int = 0,
        val trial_id: String? = null,
        val tab_position: Int = 0,
        val tab_id: Int = 0,
        val heji_params: ModAppMenu? = null,
        var type: Int = 0,
        var tid: Int = 0,
        var feature: Int = 0
    )
}