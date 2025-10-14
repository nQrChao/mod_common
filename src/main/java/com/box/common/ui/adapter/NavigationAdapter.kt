package com.box.common.ui.adapter

import android.content.*
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.box.base.base.BaseAdapter
import com.box.common.R

class NavigationAdapter constructor(context: Context) :
    AppAdapter<NavigationAdapter.MenuItem>(context), BaseAdapter.OnItemClickListener {

    private var selectedPosition: Int = 0

    private var listener: OnNavigationListener? = null

    init {
        setOnItemClickListener(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    override fun generateDefaultLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, getCount(), RecyclerView.VERTICAL, false)
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setOnNavigationListener(listener: OnNavigationListener?) {
        this.listener = listener
    }

    /**
     * [BaseAdapter.OnItemClickListener]
     */
    override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
        if (selectedPosition == position) {
            return
        }
        if (listener == null) {
            selectedPosition = position
            notifyDataSetChanged()
            return
        }
        if (listener!!.onNavigationItemSelected(position)) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder : AppViewHolder(R.layout.main_navigation_item) {

        private val iconView: ImageView? by lazy { findViewById(R.id.iv_home_navigation_icon) }
        private val titleView: TextView? by lazy { findViewById(R.id.tv_home_navigation_title) }

        override fun onBindView(position: Int) {
            getItem(position).apply {
                iconView?.setImageDrawable(getDrawable())
                titleView?.text = getText()
                iconView?.isSelected = (selectedPosition == position)
                titleView?.isSelected = (selectedPosition == position)
            }
        }
    }

    class MenuItem constructor(private val text: String?, private val drawable: Drawable?) {

        fun getText(): String? {
            return text
        }

        fun getDrawable(): Drawable? {
            return drawable
        }
    }

    interface OnNavigationListener {
        fun onNavigationItemSelected(position: Int): Boolean
    }
}