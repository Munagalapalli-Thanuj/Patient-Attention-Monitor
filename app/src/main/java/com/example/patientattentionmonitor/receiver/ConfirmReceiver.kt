package com.example.patientattentionmonitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import com.example.patientattentionmonitor.R
import com.example.patientattentionmonitor.klass.Constants
import com.example.patientattentionmonitor.klass.Pref
import com.example.patientattentionmonitor.klass.goAsync
import com.example.patientattentionmonitor.reminder.NotificationManager
import com.example.patientattentionmonitor.reminder.ReminderManager
import com.example.patientattentionmonitor.reminder.ReminderUtil
import com.example.patientattentionmonitor.repository.HistoryRepository
import com.example.patientattentionmonitor.service.FullscreenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var historyRepository: HistoryRepository

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDER_ID, -1L)
        val pillId = intent.getLongExtra(Constants.INTENT_EXTRA_PILL_ID, -1L)
        val remindedTime = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDED_TIME, -1L)

        if (reminderId == -1L || pillId == -1L || remindedTime == -1L) {
            Timber.e("Invalid number of extras passed, exiting...")
            return
        }

        var success = false
        goAsync(GlobalScope, Dispatchers.IO) {

            historyRepository.getByPillIdAndTime(pillId, remindedTime)?.let { history ->
                history.confirmed = Calendar.getInstance()
                historyRepository.updateHistoryItem(history)
                success = true
            } ?: run {
                Timber.e("Couldn't find the correct history item...")
            }

            if (Pref.alertStyle) {
                FullscreenService.stopService(context)
            }

            withContext(Dispatchers.Main) {
                if (success) {
                    // Cancel check alarm
                    val again =
                        ReminderUtil.getAlarmAgainIntent(context, reminderId, remindedTime, 0)
                    again.cancel()
                    ReminderManager.getAlarmManager(context).cancel(again)
                    // Hide notification
                    NotificationManager.cancelNotification(context, reminderId)
                    Toast.makeText(
                        context,
                        context.getString(R.string.confirmed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}