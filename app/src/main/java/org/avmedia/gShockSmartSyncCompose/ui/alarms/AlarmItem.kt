package org.avmedia.gShockSmartSyncCompose.ui.alarms

import AppSwitch
import AppText
import AppTextExtraLarge
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
import org.avmedia.gShockSmartSyncCompose.ui.common.AppTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmItem(
    time: String = stringResource(id = R.string._12_00),
    isAlarmEnabled: Boolean = true,
    onToggleAlarm: (Boolean) -> Unit
) {
    var isEnabled by remember { mutableStateOf(isAlarmEnabled) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<TimePickerState?>(null) }

    LaunchedEffect(isAlarmEnabled) {
        isEnabled = isAlarmEnabled
    }

    val handleConfirm: (TimePickerState) -> Unit = { timePickerState ->
        selectedTime = timePickerState
        showTimePickerDialog = false
        // You can also handle the selected time here
    }

    // Function to handle dismissal
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
                    text = time,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            showTimePickerDialog = true
                        },
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between texts
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
                    onDismiss = handleDismiss
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAlarmItem() {
    AlarmItem(onToggleAlarm = {})
}
