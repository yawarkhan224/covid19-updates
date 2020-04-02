package com.aryk.covid.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aryk.covid.R

/**
 * class which handles click events on simple recycler view items
 *
 * @see [https://gist.github.com/nesquena/231e356f372f214c4fe6]
 */
class ItemClickSupport private constructor(private val recyclerView: RecyclerView) {
    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    private val onClickListener = View.OnClickListener { v ->
        onItemClickListener?.let {
            val holder = recyclerView.getChildViewHolder(v)
            it.onItemClicked(recyclerView, holder.adapterPosition, v)
        }
    }

    private val onLongClickListener = View.OnLongClickListener { v ->
        onItemLongClickListener?.let {
            val holder = recyclerView.getChildViewHolder(v)
            it.onItemLongClicked(recyclerView, holder.adapterPosition, v)
        }

        false
    }

    private val attachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            onItemClickListener?.let {
                view.setOnClickListener(onClickListener)
            }

            onItemLongClickListener?.let {
                view.setOnLongClickListener(onLongClickListener)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            onItemClickListener?.let {
                view.setOnClickListener(null)
            }

            onItemLongClickListener?.let {
                view.setOnLongClickListener(null)
            }
        }
    }

    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport {
            var support = view.getTag(R.id.item_click_support) as ItemClickSupport?

            if (support == null) {
                support = ItemClickSupport(view)
                support.attach(view)
            }

            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport?

            support?.detach(view)

            return support
        }
    }

    private fun attach(view: RecyclerView) {
        view.setTag(R.id.item_click_support, this)
        view.addOnChildAttachStateChangeListener(attachListener)
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(attachListener)
        view.setTag(R.id.item_click_support, null)
        onItemClickListener = null
        onItemLongClickListener = null
    }

    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, v: View): Boolean
    }
}
