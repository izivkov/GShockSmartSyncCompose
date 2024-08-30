import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.ui.alarms.AlarmsModel
import org.avmedia.gshockapi.Alarm
import org.avmedia.gshockapi.ProgressEvents

class AlarmViewModel : ViewModel() {
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            try {
                val loadedAlarms = api().getAlarms() // Call your suspend function here
                _alarms.value = loadedAlarms
                AlarmsModel.clear()
                AlarmsModel.addAll(ArrayList(_alarms.value))
            } catch (e: Exception) {
                ProgressEvents.onNext("ApiError")
            }
        }
    }

    fun toggleAlarm(index: Int, isEnabled: Boolean) {
        val updatedAlarms = _alarms.value.toMutableList()
        updatedAlarms[index].enabled = isEnabled
        _alarms.value = updatedAlarms
    }
}
