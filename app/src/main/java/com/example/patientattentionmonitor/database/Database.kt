package com.example.patientattentionmonitor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.patientattentionmonitor.model.History
import com.example.patientattentionmonitor.model.PillEntity
import com.example.patientattentionmonitor.model.Reminder

@Database(
    entities = [PillEntity::class, Reminder::class, History::class],
    version = 6
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun getPillDao(): PillDao
    abstract fun getReminderDao(): ReminderDao
    abstract fun getHistoryDao(): HistoryDao
}