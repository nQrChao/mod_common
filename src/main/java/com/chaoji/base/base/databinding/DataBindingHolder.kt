package com.chaoji.base.base.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class DataBindingHolder<DB:ViewDataBinding>(resId: Int, parent: ViewGroup) :
    BaseViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false)) {
    private  var _binding:DB?=null
    val bind:DB get() = _binding!!
    init {
        _binding=DataBindingUtil.bind(itemView)
    }
}