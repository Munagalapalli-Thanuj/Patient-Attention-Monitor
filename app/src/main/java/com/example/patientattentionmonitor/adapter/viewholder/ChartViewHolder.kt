package com.example.patientattentionmonitor.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.PercentFormatter
import com.example.patientattentionmonitor.R
import com.example.patientattentionmonitor.databinding.ItemChartBinding
import com.example.patientattentionmonitor.klass.context
import com.example.patientattentionmonitor.klass.getAttrColor
import com.example.patientattentionmonitor.model.ChartItem

class ChartViewHolder(
    private val binding: ItemChartBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chartItem: ChartItem) = binding.run {
        chartTitle.text = chartItem.title

        pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            dragDecelerationFrictionCoef = 0.8f
            animateY(1400, Easing.EaseInOutQuad)
            holeRadius = 25f
            isDrawHoleEnabled = false
            transparentCircleRadius = 30f
            setDrawEntryLabels(true)
        }
        pieChart.legend.apply {
            textColor = context.getAttrColor(R.attr.colorOnSurface)
            isWordWrapEnabled = true
            textSize = 12f
            isEnabled = false
        }

        chartItem.pieData.setValueFormatter(PercentFormatter(pieChart))
        pieChart.data = chartItem.pieData
        pieChart.invalidate()
    }
}
