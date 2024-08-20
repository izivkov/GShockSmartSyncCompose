package org.avmedia.gShockSmartSyncCompose.ui.alarms

import AlarmViewModel
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemView
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenContainer
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle
import org.avmedia.gshockapi.Alarm
import java.util.Date

@Composable
fun AlarmList(alarmViewModel: AlarmViewModel = viewModel()) {
    val alarms by alarmViewModel.alarms.collectAsState()

    @SuppressLint("SimpleDateFormat")
    fun getTime(alarm: Alarm): String {

        fun from0to12(formattedTime: String): String {
            return if (formattedTime.startsWith("0")) {
                "12${formattedTime.substring(1)}"
            } else {
                formattedTime
            }
        }

        val sdf = SimpleDateFormat("H:mm")
        val dateObj: Date = sdf.parse(alarm.hour.toString() + ":" + alarm.minute.toString())

        val timeFormat = if (java.text.SimpleDateFormat()
                .toPattern().split(" ")[1][0] == 'h'
        ) "K:mm aa" else "H:mm"

        val time = SimpleDateFormat(timeFormat).format(dateObj)
        return if (timeFormat.contains("aa")) from0to12(time) else time
    }

    @Composable
    fun createItemList(): List<Any> {
        val alarmItems = mutableListOf<Any>()

        alarms.forEachIndexed { index, alarm ->
            ItemView {
                AlarmItem(
                    time = getTime(alarm),
                    isAlarmEnabled = alarm.enabled,
                    onToggleAlarm = { isEnabled ->
                        alarmViewModel.toggleAlarm(index, isEnabled)
                    }
                )
            }
        }

        return alarmItems.toList()
    }

    Column(
        modifier = Modifier
    ) {
        ItemList(createItemList())
    }
}

@Composable
fun AlarmsScreen(navController: NavController) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScreenContainer()
            {
                ScreenTitle(stringResource(id = R.string.watch_alarms), Modifier)

                AlarmList()

                AlarmChimeSwitch(
                    modifier = Modifier
                        .padding(4.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    ButtonsRow(
                        modifier = Modifier
                            .padding(4.dp),
                        onSendAlarmsToPhoneClick = {
                            // Handle send alarms to phone button click
                            println("Send alarms to phone clicked")
                        },
                        onSendAlarmsToWatchClick = {
                            // Handle send alarms to watch button click
                            println("Send alarms to watch clicked")
                        },
                        onInfoClick = {
                            // Handle info button click
                            println("Info button clicked")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAlarmScreen() {
    AlarmsScreen(NavController(LocalContext.current))
}