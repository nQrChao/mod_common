package com.box.common.data.model

import com.box.other.blankj.utilcode.util.StringUtils
import java.io.Serializable

data class AppletsXiaoGameFuLiInfo(
    val play_info: PlayInfo = PlayInfo(),
    val demo_info: MutableList<DemoInfo> = mutableListOf(),
    var pingtaibi: String = "",
    var fuli_status: String = "",
) : Serializable {
    fun showNewTip(): Boolean {
        return fuli_status == "1"
    }


    data class DemoInfo(
        var gameid: String = "",
        var gamename: String = "",
        var genre_ids: String = "",
        var bg_pic: String = "",
        var xgameid: String = "",
        var status: String = "0",
        var genre_str: String = "",
        var reward: String = "",
    ) : Serializable {
        fun isGet(): Boolean {
            return status == "2"
        }

        fun canGet(): Boolean {
            return status == "1"
        }

        fun titleText(): String {
            return if (status == "0") {
                StringUtils.format("首玩+%s元", reward)
            } else if (status == "1") {
                "点此领奖"
            } else {
                ""
            }
        }

        fun titleShow(): Boolean {
            return status != "2"
        }

    }

    data class PlayInfo(
        var play_time: Int = 0,
        val task_arrs: MutableList<TaskArray> = mutableListOf(),

        ) : Serializable {
        data class TaskArray(
            var id: String = "",
            var time_txt: String = "",
            var time: Int = 0,
            var reward: String = "",
            var is_get: String = "",
            var can_get: String = "",
        ) : Serializable {
            fun isGet(): Boolean {
                return is_get == "1"
            }

            fun canGet(): Boolean {
                return can_get == "1"
            }

            fun showGuang(): Boolean {
                return !isGet() && canGet()
            }

        }

    }

}