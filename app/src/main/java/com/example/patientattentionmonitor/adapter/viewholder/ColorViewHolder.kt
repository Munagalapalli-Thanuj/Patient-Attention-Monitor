package com.example.patientattentionmonitor.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.patientattentionmonitor.databinding.ItemColorBinding
import com.example.patientattentionmonitor.klass.context
import com.example.patientattentionmonitor.klass.onClick
import com.example.patientattentionmonitor.klass.setBackgroundColorShaped
import com.example.patientattentionmonitor.model.PillColor

class ColorViewHolder(
    private val binding: ItemColorBinding,
    private val listener: (View, PillColor) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pillColor: PillColor) = binding.run {
        viewPillColor.setBackgroundColorShaped(pillColor.getColor(context))
        check.isVisible = pillColor.isChecked
        pillColorFrame.onClick { view -> listener(view, pillColor) }
    }
}
