package com.example.patientattentionmonitor.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.example.patientattentionmonitor.klass.goAsync
import com.example.patientattentionmonitor.reminder.ReminderManager
import com.example.patientattentionmonitor.repository.PillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var pillRepository: PillRepository

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        goAsync(GlobalScope, Dispatchers.IO) {
            pillRepository.getAllPills().forEach {
                ReminderManager.planNextPillReminder(context, it)
            }
        }
    }
}