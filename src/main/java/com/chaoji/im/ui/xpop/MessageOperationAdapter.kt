package com.chaoji.im.ui.xpop

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chaoji.common.R
import com.chaoji.common.databinding.AdapterXpopupMessageOperationBinding
import com.chaoji.im.impl.CommonSelect

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