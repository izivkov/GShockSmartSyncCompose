package org.avmedia.gShockSmartSyncCompose.ui.alarms

import AppSwitch
import AppText
import AppTextExtraLarge
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.AppTimePicker
import org.avmedia.gshockapi.Alarm
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmItem(
    hours: Int = 12,
    minutes: Int = 0,
    isAlarmEnabled: Boolean = true,
    onToggleAlarm: (Boolean) -> Unit,
    onTimeChanged: (Int, Int) -> Unit
) {
    var isEnabled by remember { mutableStateOf(isAlarmEnabled) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<TimePickerState?>(null) }
    var alarmHours by remember { mutableStateOf(hours) }
    var alarmMinutes by remember { mutableStateOf(minutes) }

    val handleConfirm: (TimePickerState) -> Unit = { timePickerState ->
        selectedTime = timePickerState
        alarmHours = selectedTime?.hour ?: hours
        alarmMinutes = selectedTime?.minute ?: minutes
        onTimeChanged(alarmHours, alarmMinutes)
        showTimePickerDialog = false
    }

    val handleDismiss: () -> Unit = {
        showTimePickerDialog = false
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextExtraLarge(
                    text = formatTime(alarmHours, alarmMinutes),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            showTimePickerDialog = true
                        },
                )
                Spacer(modifier = Modifier.width(8.dp))
                AppText(
                    text = stringResource(id = R.string.daily),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AppSwitch(
                checked = isEnabled,
                onCheckedChange = { checked ->
                    isEnabled = checked
                    onToggleAlarm(checked)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    if (showTimePickerDialog) {
        Dialog(onDismissRequest = handleDismiss) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background) // Theme-based background color
                    .padding(16.dp) // Adjust padding as needed
            ) {
                AppTimePicker(
                    onConfirm = handleConfirm,
                    onDismiss = handleDismiss,
                    initialHour = alarmHours,
                    initialMinute = alarmMinutes,
                )
            }
        }
    }
}

fun formatTime(hours: Int, minutes: Int): String {

    fun from0to12(formattedTime: String): String {
        return if (formattedTime.startsWith("0")) {
            "12${formattedTime.substring(1)}"
        } else {
            formattedTime
        }
    }

    val sdf = SimpleDateFormat("H:mm", Locale.getDefault())
    val dateObj: Date = sdf.parse(hours.toString() + ":" + minutes.toString())

    val timeFormat = if (java.text.SimpleDateFormat()
            .toPattern().split(" ")[1][0] == 'h'
    ) "K:mm aa" else "H:mm"

    val time = SimpleDateFormat(timeFormat, Locale.getDefault()).format(dateObj)
    return if (timeFormat.contains("aa")) from0to12(time) else time
}

@Preview(showBackground = true)
@Composable
fun PreviewAlarmItem() {
    AlarmItem(onToggleAlarm = {}, onTimeChanged = { _, _ -> })
}
