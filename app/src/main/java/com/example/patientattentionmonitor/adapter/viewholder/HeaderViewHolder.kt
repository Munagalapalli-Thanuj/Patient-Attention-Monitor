package com.example.patientattentionmonitor.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.patientattentionmonitor.databinding.ItemHeaderBinding

class HeaderViewHolder(
    private val binding: ItemHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(title: String) = binding.run {
        headerText.text = title
    }
}