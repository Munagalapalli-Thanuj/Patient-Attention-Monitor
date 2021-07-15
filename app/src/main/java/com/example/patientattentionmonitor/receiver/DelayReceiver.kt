package com.example.patientattentionmonitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.example.patientattentionmonitor.klass.Constants
import com.example.patientattentionmonitor.klass.Pref
import com.example.patientattentionmonitor.klass.getTimeString
import com.example.patientattentionmonitor.reminder.NotificationManager
import com.example.patientattentionmonitor.reminder.ReminderManager
import com.example.patientattentionmonitor.reminder.ReminderUtil
import com.example.patientattentionmonitor.service.FullscreenService
import timber.log.Timber

@AndroidEntryPoint
class DelayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDER_ID, -1L)
        val remindedTime = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDED_TIME, -1L)
        val delayByMillis = intent.getLongExtra(Constants.INTENT_EXTRA_TIME_DELAY, -1L)

        if (delayByMillis == -1L || remindedTime == -1L || reminderId == -1L) {
            Timber.e("Invalid number of extras passed, exiting...")
            return
        }

        if (Pref.alertStyle) {
            FullscreenService.stopService(context)
        } else {
            // Cancel check reminder
            val again = ReminderUtil.getAlarmAgainIntent(context, reminderId, remindedTime, 0)
            again.cancel()
            ReminderManager.getAlarmManager(context).cancel(again)
        }

        ReminderManager.createCheckAlarm(
            context,
            reminderId,
            remindedTime,
            0, // Reset check counter
            delayByMillis
        )
        Timber.d(
            "Check alarm has been set to start in %s minutes",
            delayByMillis.getTimeString()
        )
        NotificationManager.cancelNotification(context, reminderId)
    }
}