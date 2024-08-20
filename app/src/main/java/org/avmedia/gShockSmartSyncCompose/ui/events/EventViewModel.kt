package org.avmedia.gShockSmartSyncCompose.ui.events

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.avmedia.gshockapi.Event
import org.avmedia.gshockapi.EventDate
import org.avmedia.gshockapi.RepeatPeriod
import java.time.DayOfWeek
import java.time.Month

class EventViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        loadEvents()
    }

    private fun loadEvents() {
        // Simulate loading data
        _events.value = listOf(
            Event(
                title = "Visit the Zoo", startDate = EventDate(2024, Month.SEPTEMBER, 2),
                endDate = EventDate(2025, Month.JANUARY, 2),
                repeatPeriod = RepeatPeriod.WEEKLY,
                daysOfWeek = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY),
                enabled = true,
                incompatible = true,
            ),
            Event(
                title = "Doctor's Visit", startDate = EventDate(2024, Month.NOVEMBER, 2),
                endDate = null,
                repeatPeriod = RepeatPeriod.NEVER,
                daysOfWeek = null,
                enabled = false,
                incompatible = true,
            ),
            Event(
                title = "Pay Rent", startDate = EventDate(2020, Month.SEPTEMBER, 1),
                endDate = null,
                repeatPeriod = RepeatPeriod.MONTHLY,
                daysOfWeek = null,
                enabled = true,
                incompatible = true,
            ),
            Event(
                title = "My Birthday", startDate = EventDate(1958, Month.JUNE, 2),
                endDate = null,
                repeatPeriod = RepeatPeriod.YEARLY,
                daysOfWeek = null,
                enabled = true,
                incompatible = true,
            ),
        )

        EventsModel.clear()
        EventsModel.addAll(ArrayList(_events.value))
    }

    fun toggleEvents(index: Int, isEnabled: Boolean) {
        val updatedAlarms = _events.value.toMutableList()
        updatedAlarms[index].enabled = isEnabled
        _events.value = updatedAlarms
    }
}
