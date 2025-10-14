package com.chaoji.im.ui.adapter

import android.content.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.chaoji.other.blankj.utilcode.util.ColorUtils
import com.chaoji.other.blankj.utilcode.util.ResourceUtils
import com.chaoji.common.R
import com.chaoji.im.data.model.AssItemInfo
import com.chaoji.im.data.model.AssistantInfo

class AssListAdapter constructor(context: Context) : AppAdapter<AssistantInfo?>(context) {

    inner class ViewHolder : AppViewHolder(R.layout.item_ass_list) {

        private val titleView: TextView? by lazy { findViewById(R.id.ass_item_title) }
        private val cardView: CardView? by lazy { findViewById(R.id.card) }
        private val contentView: TextView? by lazy { findViewById(R.id.ass_item_content) }
        private val iconView: ImageView? by lazy { findViewById(R.id.ass_item_icon) }

        override fun onBindView(position: Int) {
            getItem(position)?.let { ColorUtils.string2Int(it.color) }?.let { cardView?.setCardBackgroundColor(it) }
            titleView?.text = getItem(position)?.title
            contentView?.text = getItem(position)?.info
            //iconView?.setImageDrawable(getItem(position)?.icon?.let { ResourceUtils.getDrawable(it) })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }
}