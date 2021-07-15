package com.example.patientattentionmonitor.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.patientattentionmonitor.adapter.viewholder.EmptyViewHolder
import com.example.patientattentionmonitor.adapter.viewholder.HistoryItemViewHolder
import com.example.patientattentionmonitor.databinding.ItemHistoryBinding
import com.example.patientattentionmonitor.databinding.LayoutViewEmptyBinding
import com.example.patientattentionmonitor.klass.getItemOrNull
import com.example.patientattentionmonitor.model.BaseModel
import com.example.patientattentionmonitor.model.BaseModel.ItemType
import com.example.patientattentionmonitor.model.EmptyItem
import com.example.patientattentionmonitor.model.History
import java.util.*

class HistoryViewAdapter(
    private val emptyDrawable: Drawable?,
    private val showNames: Boolean
) : ListAdapter<BaseModel, RecyclerView.ViewHolder>(BaseModel.DiffCallback) {


    private var itemOptionsClickListener: (View, History, Int) -> Unit = { _, _, _ -> }

    fun setItemOptionsClickListener(listener: (View, History, Int) -> Unit) {
        itemOptionsClickListener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ItemType.HISTORY_ITEM.ordinal -> {
                if (holder is HistoryItemViewHolder) {
                    val currentHistoryItem = getItem(position) as History
                    var isFirstOfDate = true
                    (getItemOrNull(position - 1) as History?)?.let {
                        val day = it.reminded.get(Calendar.DAY_OF_YEAR)
                        val dayCurrent =
                            currentHistoryItem.reminded.get(Calendar.DAY_OF_YEAR)
                        if (dayCurrent == day) {
                            isFirstOfDate = false
                        }
                    }
                    holder.bind(
                        currentHistoryItem,
                        currentList.firstOrNull() == currentHistoryItem,
                        isFirstOfDate,
                        position,
                        showNames
                    )
                }
            }
            ItemType.EMPTY.ordinal -> if (holder is EmptyViewHolder) holder.bind()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemType.EMPTY.ordinal -> EmptyViewHolder(
                LayoutViewEmptyBinding.inflate(layoutInflater, parent, false),
                "", emptyDrawable
            )
            ItemType.HISTORY_ITEM.ordinal -> HistoryItemViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false),
                itemOptionsClickListener
            )
            else -> throw RuntimeException("Unknown ViewHolder")
        }
    }

    override fun submitList(list: List<BaseModel>?) = submitList(list, null)

    override fun submitList(list: List<BaseModel>?, commitCallback: Runnable?) {
        list?.let { ll ->
            if (ll.isEmpty()) {
                val newList = list.toMutableList()
                newList.add(EmptyItem())
                super.submitList(newList, commitCallback)
            } else {
                super.submitList(list, commitCallback)
            }
        } ?: super.submitList(list, commitCallback)
    }

    override fun getItemViewType(position: Int) = getItem(position).itemType.ordinal
}