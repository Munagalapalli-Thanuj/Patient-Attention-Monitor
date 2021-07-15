package com.example.patientattentionmonitor.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.patientattentionmonitor.R
import com.example.patientattentionmonitor.databinding.ItemHistoryPillBinding
import com.example.patientattentionmonitor.klass.context
import com.example.patientattentionmonitor.klass.onClick
import com.example.patientattentionmonitor.klass.setBackgroundColorShaped
import com.example.patientattentionmonitor.model.BaseModel
import com.example.patientattentionmonitor.model.HistoryOverallItem
import com.example.patientattentionmonitor.model.HistoryPillItem
import com.example.patientattentionmonitor.model.Pill

class HistoryViewHolder(
    private val binding: ItemHistoryPillBinding,
    private val clickListener: (View, BaseModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(historyPill: HistoryPillItem) = binding.run {
        when (historyPill.historyType) {
            is HistoryOverallItem -> {
                viewPillColor.isVisible = false
                textHistoryName.text = binding.root.context.getString(R.string.stat_overall)
            }
            is Pill -> {
                viewPillColor.isVisible = true
                viewPillColor.setBackgroundColorShaped(historyPill.historyType.colorResource(context))
                textHistoryName.text = historyPill.historyType.name
            }
        }

        cardHistoryPill.onClick { view ->
            clickListener(view, historyPill.historyType)
        }

        textHistoryDescription.text = historyPill.stat.getSummaryText(context)
        textHistoryDescription.isVisible = historyPill.stat.hasStats
    }
}
