package com.box.common.ui.xpop

import com.box.com.R
import com.box.com.databinding.AdapterXpopupMessageOperationBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.box.common.impl.CommonSelect

class MessageOperationAdapter(list: MutableList<CommonSelect>):BaseQuickAdapter<CommonSelect,BaseDataBindingHolder<AdapterXpopupMessageOperationBinding>>(
    R.layout.adapter_xpopup_message_operation,list) {
    override fun convert(
        holder: BaseDataBindingHolder<AdapterXpopupMessageOperationBinding>,
        item: CommonSelect
    ) {
        holder.dataBinding?.run {
            image.setImageResource(item.getLeftImageRes())
            text.text=item.getText()
        }
    }
}