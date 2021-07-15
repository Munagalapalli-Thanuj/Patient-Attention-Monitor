package com.example.patientattentionmonitor.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.patientattentionmonitor.databinding.ItemReminderBinding
import com.example.patientattentionmonitor.klass.context
import com.example.patientattentionmonitor.klass.onClick
import com.example.patientattentionmonitor.model.Reminder

class ReminderViewHolder(
    private val binding: ItemReminderBinding,
    private val clickListener: (View, Reminder) -> Unit,
    private val deleteListener: (View, Reminder) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(reminder: Reminder, showDelete: Boolean, showRipple: Boolean) = binding.run {
        textReminderTime.text = reminder.formattedString(context)

        buttonDeleteReminder.isVisible = showDelete
        if (!showRipple) reminderContainer.background = null

        reminderContainer.onClick { view -> clickListener(view, reminder) }
        buttonDeleteReminder.onClick { view -> deleteListener(view, reminder) }
    }
}
