package com.example.patientattentionmonitor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.patientattentionmonitor.adapter.viewholder.ColorViewHolder
import com.example.patientattentionmonitor.databinding.ItemColorBinding
import com.example.patientattentionmonitor.model.PillColor

class ColorAdapter : ListAdapter<PillColor, ColorViewHolder>(PillColor.DiffCallback) {

    private var clickListener: (View, PillColor) -> Unit = { _, _ -> }

    fun setOnColorClickedListener(listener: (View, PillColor) -> Unit) {
        clickListener = listener
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ColorViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )

}