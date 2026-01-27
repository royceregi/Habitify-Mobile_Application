package com.royce.habitify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter class for RecyclerView with common functionality
 * Extracted from ExpenseAura's adapter patterns
 */
abstract class BaseAdapter<T>(
    protected val layoutResId: Int
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    protected val items = mutableListOf<T>()

    fun submitList(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(position: Int, item: T) {
        if (position in items.indices) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): T? {
        return if (position in items.indices) items[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // This method should be overridden in subclasses
        throw NotImplementedError("Subclasses must implement onCreateViewHolder")
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        open fun onItemClick(position: Int) {
            // Override in subclasses
        }
    }
}
