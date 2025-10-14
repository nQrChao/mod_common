package com.chaoji.im.ui.xpop.pay

import androidx.appcompat.widget.AppCompatCheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chaoji.im.data.model.PayInfo
import com.chaoji.common.BR
import com.chaoji.common.R
import com.chaoji.common.databinding.ItemPayListVBinding

class AdapterXPopupPay constructor(list: MutableList<PayInfo>) : BaseQuickAdapter<PayInfo, BaseDataBindingHolder<ItemPayListVBinding>>(
    R.layout.item_pay_list_v, list) {
    companion object {
        const val ITEM_CHOICE = 100001
    }

    override fun convert(holder: BaseDataBindingHolder<ItemPayListVBinding>, item: PayInfo) {
        holder.dataBinding?.setVariable(BR.payInfo,item)
    }

    override fun convert(holder: BaseDataBindingHolder<ItemPayListVBinding>, item: PayInfo, payloads: List<Any>) {
        for (p in payloads) {
            val payload = p as Int
            if (payload == ITEM_CHOICE) {
                val choiceView: AppCompatCheckBox = holder.itemView.findViewById(R.id.item_check)
                choiceView.isChecked = item.isChoice
            }
        }
    }

}