package com.chaoji.im.ui.adapter

import android.content.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chaoji.other.blankj.utilcode.util.ResourceUtils
import com.chaoji.common.R
import com.chaoji.im.data.model.AssItemInfo

class NotifyMessageAdapter constructor(context: Context) : AppAdapter<AssItemInfo?>(context) {

    inner class ViewHolder : AppViewHolder(R.layout.item_ass_list) {

        private val titleView: TextView? by lazy { findViewById(R.id.ass_item_title) }
        private val contentView: TextView? by lazy { findViewById(R.id.ass_item_content) }
        private val iconView: ImageView? by lazy { findViewById(R.id.ass_item_icon) }

        override fun onBindView(position: Int) {
            titleView?.text = getItem(position)?.title
            contentView?.text = getItem(position)?.content
            iconView?.setImageDrawable(getItem(position)?.icon?.let { ResourceUtils.getDrawable(it) })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }
}