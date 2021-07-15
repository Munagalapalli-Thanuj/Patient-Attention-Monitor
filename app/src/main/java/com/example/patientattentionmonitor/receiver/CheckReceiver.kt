package com.example.patientattentionmonitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.example.patientattentionmonitor.klass.Constants
import com.example.patientattentionmonitor.klass.Pref
import com.example.patientattentionmonitor.klass.goAsync
import com.example.patientattentionmonitor.reminder.ReminderManager
import com.example.patientattentionmonitor.reminder.ReminderUtil
import com.example.patientattentionmonitor.repository.HistoryRepository
import com.example.patientattentionmonitor.repository.PillRepository
import com.example.patientattentionmonitor.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CheckReceiver : BroadcastReceiver() {

    @Inject
    lateinit var pillRepository: PillRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var historyRepository: HistoryRepository

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDER_ID, -1L)
        val remindedTime = intent.getLongExtra(Constants.INTENT_EXTRA_REMINDED_TIME, -1L)
        val checkCount = intent.getLongExtra(Constants.INTENT_CHECK_COUNT, -1L)

        if (remindedTime == -1L || reminderId == -1L || checkCount == -1L) {
            Timber.e("Invalid number of extras passed, exiting...")
            return
        }

        Timber.d(
            "Received reminder id: %d, remindedTime: %d, checkCount: %d",
            reminderId,
            remindedTime,
            checkCount
        )

        goAsync(GlobalScope, Dispatchers.IO) {
            val reminder = reminderRepository.getReminder(reminderId)
            val pill = pillRepository.getPill(reminder.pillId)

            ReminderUtil.createReminderNotification(context, pill, reminder)

            // If this reminder has not been confirmed and remindAgain is enabled, schedule check alarm
            historyRepository.getByPillIdAndTime(pill.id, remindedTime)?.let { history ->
                if (Pref.remindAgain && !history.hasBeenConfirmed && checkCount < Constants.MAX_CHECK_COUNT - 1) {
                    ReminderManager.createCheckAlarm(
                        context,
                        reminderId,
                        remindedTime,
                        checkCount + 1
                    )
                }
            }
        }
    }
}