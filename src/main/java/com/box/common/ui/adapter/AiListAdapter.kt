package com.box.common.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.box.common.data.model.AIChat
import com.box.common.R
import com.box.common.databinding.ItemAiListBinding

/**
 *    desc   : 数据列表
 */
class AiListAdapter constructor(list: MutableList<AIChat>) : BaseQuickAdapter<AIChat, BaseDataBindingHolder<ItemAiListBinding>>(
    R.layout.item_ai_list, list) {

    override fun convert(holder: BaseDataBindingHolder<ItemAiListBinding>, item: AIChat) {
        holder.dataBinding?.let {
            it.title.text = item.tittle
            if (item.select)
                it.parentLayout.setBackgroundResource(R.drawable.button_around_green2_selector)
            else
                it.parentLayout.setBackgroundResource(R.color.transparent)
        }

    }
}