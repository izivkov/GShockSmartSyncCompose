package org.avmedia.gShockSmartSyncCompose.ui.common

import TimeFormat
import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    initialHour: Int,
    initialMinute: Int,
) {
    val timeFormat = if (DateFormat.is24HourFormat(applicationContext())) {
        TimeFormat.TwentyFourHour
    } else {
        TimeFormat.TwelveHour
    }

    val timePickerState = rememberTimePickerState(
        initialHour,
        initialMinute,
        is24Hour = timeFormat == TimeFormat.TwentyFourHour,
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background) // Theme-based background color
            .padding(16.dp) // Consistent padding
    ) {
        TimePicker(
            state = timePickerState,
            modifier = Modifier.padding(vertical = 16.dp) // Padding around the time picker
        )
        Spacer(modifier = Modifier.height(8.dp)) // Spacing between buttons
        Row {
            AppButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                text = "Cancel"
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between buttons
            AppButton(
                onClick = { onConfirm(timePickerState) },
                modifier = Modifier.weight(1f), // Equal width buttons
                text = "OK"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewInputExample() {
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }

    AppTimePicker(
        onDismiss = {
        },
        onConfirm = { time ->
            selectedTime = time
        },
        initialHour = 15,
        initialMinute = 23
    )
}