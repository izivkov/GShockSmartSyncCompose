import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.avmedia.gShockSmartSyncCompose.ui.alarms.AlarmsModel
import org.avmedia.gshockapi.Alarm

class AlarmViewModel : ViewModel() {
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        // Simulate loading data
        _alarms.value = listOf(
            Alarm(6, 45, enabled = true, hasHourlyChime = true),
            Alarm(8, 0, enabled = true, hasHourlyChime = false),
            Alarm(20, 0, enabled = true, hasHourlyChime = false),
            Alarm(0, 0, enabled = false, hasHourlyChime = false),
            Alarm(0, 0, enabled = false, hasHourlyChime = false),
        )

        AlarmsModel.clear()
        AlarmsModel.addAll(ArrayList(_alarms.value))
    }

    fun toggleAlarm(index: Int, isEnabled: Boolean) {
        val updatedAlarms = _alarms.value.toMutableList()
        updatedAlarms[index].enabled = isEnabled
        _alarms.value = updatedAlarms
    }
}

// data class Alarm(val time: String, val isEnabled: Boolean)
