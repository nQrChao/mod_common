package com.chaoji.im.data.model

import java.io.Serializable

data class ModGameHallTabList(
    val genre_list: MutableList<Genre> = mutableListOf(),
    val search_list: MutableList<Genre> = mutableListOf(),
    ) : Serializable {
    data class Genre(
        val genre_id: String? = null,
        val genre_name: String? = null,
        val type: String? = null,
    )

}

