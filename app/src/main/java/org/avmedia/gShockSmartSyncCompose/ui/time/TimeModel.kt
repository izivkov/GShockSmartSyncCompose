package org.avmedia.gShockSmartSyncCompose.ui.time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gshockapi.ProgressEvents

class TimeModel : ViewModel() {
    private val _timer = MutableStateFlow(0)
    val timer = _timer

    private val _homeTime = MutableStateFlow("")
    val homeTime = _homeTime

    private val _batteryLevel = MutableStateFlow(0)
    val batteryLevel = _batteryLevel

    private val _temperature = MutableStateFlow(0)
    val temperature = _temperature

    private val _watchName = MutableStateFlow("")
    val watchName = _watchName

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