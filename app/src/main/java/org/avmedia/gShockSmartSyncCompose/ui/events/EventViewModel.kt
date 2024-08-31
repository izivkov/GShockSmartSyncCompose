package org.avmedia.gShockSmartSyncCompose.ui.events

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.avmedia.gshockapi.Event
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents

class EventViewModel() : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    fun loadEvents(context: Context) {
        viewModelScope.launch {
            try {
                val loadedEvents = CalendarEvents.getEventsFromCalendar(context)
                _events.value = loadedEvents
                EventsModel.refresh(context)
            } catch (e: Exception) {
                ProgressEvents.onNext("ApiError", e.message)
            }
        }
    }

    fun toggleEvents(index: Int, isEnabled: Boolean) {
        val updatedEvents = _events.value.toMutableList()
        updatedEvents[index].enabled = isEnabled
        _events.value = updatedEvents
    }
}
