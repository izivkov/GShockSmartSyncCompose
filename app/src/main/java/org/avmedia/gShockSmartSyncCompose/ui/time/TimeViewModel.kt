package org.avmedia.gShockSmartSyncCompose.ui.time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gshockapi.ProgressEvents

class TimeViewModel : ViewModel() {
    private val _timer = MutableStateFlow(0)
    val timer = _timer
    fun setTimer(hours: Int, minutes: Int, seconds: Int) {
        _timer.value = hours * 3600 + minutes * 60 + seconds
    }

    private val _homeTime = MutableStateFlow("")
    val homeTime = _homeTime

    private val _batteryLevel = MutableStateFlow(0)
    val batteryLevel = _batteryLevel

    private val _temperature = MutableStateFlow(0)
    val temperature = _temperature

    private val _watchName = MutableStateFlow("")
    val watchName = _watchName

    fun sendTimerToWatch(timeMs: Int) {
        viewModelScope.launch {
            api().setTimer(timeMs)
        }
    }

    fun sendTimeToWatch() {
        viewModelScope.launch {
            try {
                api().setTime()
            } catch (e: Exception) {
                ProgressEvents.onNext("ApiError", e.message ?: "")
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                _timer.value = api().getTimer()
                _homeTime.value = api().getHomeTime()
                _batteryLevel.value = api().getBatteryLevel()
                _temperature.value = api().getWatchTemperature()
                _watchName.value = api().getWatchName()
            } catch (e: Exception) {
                ProgressEvents.onNext("ApiError")
            }
        }
    }
}