package com.chaoji.im.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class WrapRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(context, attrs, defStyleAttr) {
    private var realAdapter: Adapter<out ViewHolder>? = null
    private var wrapAdapter: WrapRecyclerAdapter = WrapRecyclerAdapter()

    override fun setAdapter(adapter: Adapter<out ViewHolder>?) {
        realAdapter = adapter
        wrapAdapter.setRealAdapter(realAdapter)
        itemAnimator = null
        super.setAdapter(wrapAdapter)
    }

    override fun getAdapter(): Adapter<*>? {
        return realAdapter
    }

    fun addHeaderView(view: View) {
        wrapAdapter.addHeaderView(view)
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : View?> addHeaderView(@LayoutRes id: Int): V {
        val headerView: View = LayoutInflater.from(context).inflate(id, this, false)
        addHeaderView(headerView)
        return headerView as V
    }

    fun removeHeaderView(view: View) {
        wrapAdapter.removeHeaderView(view)
    }

    fun addFooterView(view: View) {
        wrapAdapter.addFooterView(view)
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : View?> addFooterView(@LayoutRes id: Int): V {
        val footerView: View = LayoutInflater.from(context).inflate(id, this, false)
        addFooterView(footerView)
        return footerView as V
    }

    fun removeFooterView(view: View) {
        wrapAdapter.removeFooterView(view)
    }

    fun getHeaderViewsCount(): Int {
        return wrapAdapter.getHeaderViewsCount()
    }

    fun getFooterViewsCount(): Int {
        return wrapAdapter.getFooterViewsCount()
    }

    fun getHeaderViews(): MutableList<View?> {
        return wrapAdapter.getHeaderViews()
    }

    fun getFooterViews(): MutableList<View?> {
        return wrapAdapter.getFooterViews()
    }

    fun refreshHeaderFooterViews() {
        wrapAdapter.notifyDataSetChanged()
    }

    fun adjustSpanSize() {
        val layoutManager: LayoutManager? = layoutManager
        if (layoutManager !is GridLayoutManager) {
            return
        }

        layoutManager.spanSizeLookup = object : SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return if (((position < wrapAdapter.getHeaderViewsCount()
                            || position >= wrapAdapter.getHeaderViewsCount() + (if (realAdapter == null) 0 else realAdapter!!.itemCount)))
                ) layoutManager.spanCount else 1
            }
        }
    }

    private class WrapRecyclerAdapter : Adapter<ViewHolder?>() {

        companion object {
            private const val HEADER_VIEW_TYPE: Int = Int.MIN_VALUE shr 1
            private const val FOOTER_VIEW_TYPE: Int = Int.MAX_VALUE shr 1
        }

        private var realAdapter: Adapter<ViewHolder>? = null
        private val headerViews: MutableList<View?> = ArrayList()
        private val footerViews: MutableList<View?> = ArrayList()
        private var currentPosition: Int = 0
        private var recyclerView: RecyclerView? = null
        private var observer: WrapAdapterDataObserver? = null

        @Suppress("UNCHECKED_CAST")
        fun setRealAdapter(adapter: Adapter<out ViewHolder>?) {
            if (realAdapter === adapter) {
                return
            }
            if (realAdapter != null) {
                if (observer != null) {
                    realAdapter!!.unregisterAdapterDataObserver(observer!!)
                }
            }
            realAdapter = adapter as Adapter<ViewHolder>?
            if (realAdapter == null) {
                return
            }
            if (observer == null) {
                observer = WrapAdapterDataObserver(this)
            }
            realAdapter?.registerAdapterDataObserver(observer!!)
            if (recyclerView != null) {
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int {
            var itemCount = 0
            if (realAdapter != null) {
                itemCount = realAdapter!!.itemCount
            }
            return getHeaderViewsCount() + itemCount + getFooterViewsCount()
        }

        override fun getItemViewType(position: Int): Int {
            currentPosition = position
            val headerCount: Int = getHeaderViewsCount()
            val adapterCount: Int = if (realAdapter != null) realAdapter!!.itemCount else 0
            val adjPosition: Int = position - headerCount
            if (position < headerCount) {
                return HEADER_VIEW_TYPE
            } else if (adjPosition < adapterCount) {
                return realAdapter!!.getItemViewType(adjPosition)
            }
            return FOOTER_VIEW_TYPE
        }

        fun getPosition(): Int {
            return currentPosition
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return when (viewType) {
                HEADER_VIEW_TYPE -> newWrapViewHolder(headerViews[getPosition()]!!)
                FOOTER_VIEW_TYPE -> newWrapViewHolder(footerViews[getPosition() - getHeaderViewsCount() - (if (realAdapter != null) realAdapter!!.itemCount else 0)]!!)
                else -> {
                    val itemViewType: Int = realAdapter!!.getItemViewType(getPosition() - getHeaderViewsCount())
                    if (itemViewType == HEADER_VIEW_TYPE || itemViewType == FOOTER_VIEW_TYPE) {
                        throw IllegalStateException("Please do not use this type as itemType")
                    }
                    realAdapter!!.onCreateViewHolder(parent, itemViewType)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (realAdapter == null) {
                return
            }
            when (getItemViewType(position)) {
                HEADER_VIEW_TYPE, FOOTER_VIEW_TYPE -> {}
                else -> realAdapter!!.onBindViewHolder(holder, getPosition() - getHeaderViewsCount())
            }
        }

        private fun newWrapViewHolder(view: View): WrapViewHolder {
            val parent: ViewParent? = view.parent
            if (parent is ViewGroup) {
                parent.removeView(view)
            }
            return WrapViewHolder(view)
        }

        override fun getItemId(position: Int): Long {
            if ((realAdapter != null) && (position > getHeaderViewsCount() - 1) && (position < getHeaderViewsCount() + realAdapter!!.itemCount)) {
                return realAdapter!!.getItemId(position - getHeaderViewsCount())
            }
            return super.getItemId(position)
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            this.recyclerView = recyclerView
            realAdapter?.onAttachedToRecyclerView(recyclerView)
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            this.recyclerView = null
            realAdapter?.onDetachedFromRecyclerView(recyclerView)
        }

        override fun onViewRecycled(holder: ViewHolder) {
            if (holder is WrapViewHolder) {
                holder.setIsRecyclable(false)
                return
            }
            realAdapter?.onViewRecycled(holder)
        }

        override fun onFailedToRecycleView(holder: ViewHolder): Boolean {
            if (realAdapter == null) {
                return super.onFailedToRecycleView(holder)
            }
            return realAdapter!!.onFailedToRecycleView(holder)
        }

        override fun onViewAttachedToWindow(holder: ViewHolder) {
            if (realAdapter == null) {
                return
            }
            realAdapter!!.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder) {
            realAdapter?.onViewDetachedFromWindow(holder)
        }

        fun addHeaderView(view: View) {
            if (!headerViews.contains(view) && !footerViews.contains(view)) {
                headerViews.add(view)
                notifyDataSetChanged()
            }
        }

        fun removeHeaderView(view: View) {
            if (headerViews.remove(view)) {
                notifyDataSetChanged()
            }
        }

        fun addFooterView(view: View) {
            if (!footerViews.contains(view) && !headerViews.contains(view)) {
                footerViews.add(view)
                notifyDataSetChanged()
            }
        }

        fun removeFooterView(view: View) {
            if (footerViews.remove(view)) {
                notifyDataSetChanged()
            }
        }

        fun getHeaderViewsCount(): Int {
            return headerViews.size
        }

        fun getFooterViewsCount(): Int {
            return footerViews.size
        }

        fun getHeaderViews(): MutableList<View?> {
            return headerViews
        }

        fun getFooterViews(): MutableList<View?> {
            return footerViews
        }
    }

    private class WrapViewHolder constructor(itemView: View) : ViewHolder(itemView)

    private class WrapAdapterDataObserver constructor(private val wrapAdapter: WrapRecyclerAdapter) : AdapterDataObserver() {

        override fun onChanged() {
            wrapAdapter.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            wrapAdapter.notifyItemRangeChanged(wrapAdapter.getHeaderViewsCount() + positionStart, itemCount, payload)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            wrapAdapter.notifyItemRangeChanged(wrapAdapter.getHeaderViewsCount() + positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            wrapAdapter.notifyItemRangeInserted(wrapAdapter.getHeaderViewsCount() + positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            wrapAdapter.notifyItemRangeRemoved(wrapAdapter.getHeaderViewsCount() + positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            wrapAdapter.notifyItemMoved(wrapAdapter.getHeaderViewsCount() + fromPosition, wrapAdapter.getHeaderViewsCount() + toPosition)
        }
    }
}