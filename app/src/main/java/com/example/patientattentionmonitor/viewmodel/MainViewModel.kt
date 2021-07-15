package com.example.patientattentionmonitor.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.patientattentionmonitor.reminder.ReminderManager
import com.example.patientattentionmonitor.repository.PillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pillRepository: PillRepository
) : ViewModel() {

    var wasInDetails = false
    var wasInNewPill = false

    // Plan all pills when opened, maybe wasteful?
    fun planReminders(applicationContext: Context) = viewModelScope.launch(Dispatchers.Main) {
        pillRepository.getAllPills().forEach {
            ReminderManager.planNextPillReminder(applicationContext, it)
        }
    }

    private val _shouldScrollUp = MutableLiveData(Unit)
    val shouldScrollUp: LiveData<Unit>
        get() = _shouldScrollUp

    fun scrollUp() {
        _shouldScrollUp.value = Unit
    }

}