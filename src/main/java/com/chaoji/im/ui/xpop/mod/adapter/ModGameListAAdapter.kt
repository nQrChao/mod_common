package com.chaoji.im.ui.xpop.mod.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chaoji.common.BR
import com.chaoji.common.R
import com.chaoji.common.databinding.ModItemGameListBinding
import com.chaoji.im.data.model.ModGameAppletsInfo

class ModGameListAAdapter constructor(list: MutableList<ModGameAppletsInfo>) : BaseQuickAdapter<ModGameAppletsInfo, BaseDataBindingHolder<ModItemGameListBinding>>(
    R.layout.mod_item_game_list, list) {
    override fun convert(holder: BaseDataBindingHolder<ModItemGameListBinding>, item: ModGameAppletsInfo) {
        holder.dataBinding?.setVariable(BR.gameApplet, item)
    }

}