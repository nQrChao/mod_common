package com.box.common.data.model

import androidx.databinding.BaseObservable
import java.io.Serializable

data class ModSearchHotGameBean(
    val s_best_title: String = "",
    val s_best_title_show: String = "",
    val game_type: String = "",
    val search_hot_word: MutableList<String> = mutableListOf(),
    val s_best: MutableList<SearchBest> = mutableListOf(),
) : BaseObservable(), Serializable {

    data class SearchBest(
        val page_type: String = "",
        val icon: String = "",
        val jump_target: String = "",
        val client_type: String = "",
        val title: String = "",
        val t_type: String = "",
        val title2: String = "",
        val sort: String = "",
        val param: SearchBestParam ,
    ) : Serializable{
        data class SearchBestParam(
            val gameid: String = "",
            val game_type: String = "",
        ) : Serializable
    }

    data class PicList(
        val pid: String = "",
        val gid: String = "",
        val pic_path: String = "",
        val pic_width: String = "",
        val pic_height: String = "",
    ) : Serializable
}

