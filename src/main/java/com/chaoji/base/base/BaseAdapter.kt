package com.chaoji.base.base

import android.content.*
import android.util.SparseArray
import android.view.*
import android.view.View.OnLongClickListener
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoji.base.base.action.ResourcesAction

@Suppress("LeakingThis")
abstract class BaseAdapter<VH : BaseAdapter<VH>.BaseViewHolder> (private val context: Context) :
    RecyclerView.Adapter<VH>(), ResourcesAction {

    private var recyclerView: RecyclerView? = null

    private var itemClickListener: OnItemClickListener? = null

    private var itemLongClickListener: OnItemLongClickListener? = null

    private val childClickListeners: SparseArray<OnChildClickListener?> by lazy { SparseArray() }

    private val childLongClickListeners: SparseArray<OnChildLongClickListener?> by lazy { SparseArray() }

    private var positionOffset: Int = 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        positionOffset = position - holder.adapterPosition
        holder.onBindView(position)
    }

    open fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    override fun getContext(): Context {
        return context
    }

    abstract inner class BaseViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {

        constructor(@LayoutRes id: Int) : this(
            LayoutInflater.from(getContext()).inflate(id, recyclerView, false)
        )

        init {
            if (itemClickListener != null) {
                itemView.setOnClickListener(this)
            }

            if (itemLongClickListener != null) {
                itemView.setOnLongClickListener(this)
            }

            for (i in 0 until childClickListeners.size()) {
                findViewById<View>(childClickListeners.keyAt(i))?.setOnClickListener(this)
            }

            for (i in 0 until childLongClickListeners.size()) {
                findViewById<View>(childLongClickListeners.keyAt(i))?.setOnLongClickListener(this)
            }
        }

        abstract fun onBindView(position: Int)

        protected open fun getViewHolderPosition(): Int {
            return layoutPosition + positionOffset
        }

        /**
         * [View.OnClickListener]
         */
        override fun onClick(view: View) {
            val position: Int = getViewHolderPosition()
            if (position < 0 || position >= itemCount) {
                return
            }
            if (view === getItemView()) {
                itemClickListener?.onItemClick(recyclerView, view, position)
                return
            }
            childClickListeners.get(view.id)?.onChildClick(recyclerView, view, position)
        }

        /**
         * [View.OnLongClickListener]
         */
        override fun onLongClick(view: View): Boolean {
            val position: Int = getViewHolderPosition()
            if (position < 0 || position >= itemCount) {
                return false
            }
            if (view === getItemView()) {
                if (itemLongClickListener != null) {
                    return itemLongClickListener!!.onItemLongClick(recyclerView, view, position)
                }
                return false
            }
            val listener: OnChildLongClickListener? = childLongClickListeners.get(view.id)
            if (listener != null) {
                return listener.onChildLongClick(recyclerView, view, position)
            }
            return false
        }

        open fun getItemView(): View {
            return itemView
        }

        open fun <V : View?> findViewById(@IdRes id: Int): V? {
            return getItemView().findViewById(id)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        if (this.recyclerView?.layoutManager == null) {
            this.recyclerView?.layoutManager = generateDefaultLayoutManager(context)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    protected open fun generateDefaultLayoutManager(context: Context): RecyclerView.LayoutManager? {
        return LinearLayoutManager(context)
    }

    open fun setOnItemClickListener(listener: OnItemClickListener?) {
        checkRecyclerViewState()
        itemClickListener = listener
    }

    open fun setOnChildClickListener(@IdRes id: Int, listener: OnChildClickListener?) {
        checkRecyclerViewState()
        childClickListeners.put(id, listener)
    }

    open fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        checkRecyclerViewState()
        itemLongClickListener = listener
    }

    open fun setOnChildLongClickListener(@IdRes id: Int, listener: OnChildLongClickListener?) {
        checkRecyclerViewState()
        childLongClickListeners.put(id, listener)
    }

    private fun checkRecyclerViewState() {
        if (recyclerView != null) {
            throw IllegalStateException("checkRecyclerViewState?")
        }
    }

    interface OnItemClickListener {
        fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int)
    }


    interface OnItemLongClickListener {

        fun onItemLongClick(recyclerView: RecyclerView?, itemView: View?, position: Int): Boolean
    }

    interface OnChildClickListener {

        fun onChildClick(recyclerView: RecyclerView?, childView: View?, position: Int)
    }

    interface OnChildLongClickListener {

        fun onChildLongClick(recyclerView: RecyclerView?, childView: View?, position: Int): Boolean
    }
}