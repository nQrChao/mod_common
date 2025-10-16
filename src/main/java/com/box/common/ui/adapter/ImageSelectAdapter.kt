package com.box.common.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.box.com.R
import com.box.common.glide.GlideApp

class ImageSelectAdapter constructor(context: Context, private val mSelectImages: MutableList<String>) : AppAdapter<String>(context) {
    inner class ViewHolder : AppViewHolder(R.layout.image_select_item) {

        private val imageView: ImageView? by lazy { findViewById(R.id.iv_image_select_image) }
        private val checkBox: CheckBox? by lazy { findViewById(R.id.cb_image_select_check) }

        override fun onBindView(position: Int) {
            getItem(position).apply {
                imageView?.let {
                    GlideApp.with(getContext())
                        .asBitmap()
                        .load(this)
                        .into(it)
                }
                checkBox?.isChecked = mSelectImages.contains(this)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    override fun generateDefaultLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 3)
    }
}