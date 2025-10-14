package com.box.common.ui.xpop.mod.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.box.common.BR
import com.box.common.R
import com.box.common.databinding.ModItemGameListBinding
import com.box.common.data.model.ModGameAppletsInfo

class ModGameListAAdapter constructor(list: MutableList<ModGameAppletsInfo>) : BaseQuickAdapter<ModGameAppletsInfo, BaseDataBindingHolder<ModItemGameListBinding>>(
    R.layout.mod_item_game_list, list) {
    override fun convert(holder: BaseDataBindingHolder<ModItemGameListBinding>, item: ModGameAppletsInfo) {
        holder.dataBinding?.setVariable(BR.gameApplet, item)
    }

}