package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.ui.common.AppButton

@Composable
fun TimerPicker(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onDismiss: () -> Unit,
    onSubmit: (Int, Int, Int) -> Unit
) {
    var hourInput by remember { mutableStateOf(hours.toString().padStart(2, '0')) }
    var minuteInput by remember { mutableStateOf(minutes.toString().padStart(2, '0')) }
    var secondInput by remember { mutableStateOf(seconds.toString().padStart(2, '0')) }

    val isInputValid = remember(hourInput, minuteInput, secondInput) {
        val hourValid = hourInput.toIntOrNull() in 0..23
        val minuteValid = minuteInput.toIntOrNull() in 0..59
        val secondValid = secondInput.toIntOrNull() in 0..59
        hourValid && minuteValid && secondValid
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { AppText(text = "Enter Timer Time") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedTextField(
                        value = hourInput,
                        onValueChange = { input ->
                            if (input.length <= 2 && input.all { it.isDigit() }) {
                                hourInput = input
                            }
                        },
                        label = { Text(text = "Hours") },
                        placeholder = { Text(text = "HH") },
                        singleLine = true,
                        isError = hourInput.toIntOrNull() !in 0..23,
                        modifier = Modifier.width(80.dp)
                    )
                    Text(text = " : ", style = MaterialTheme.typography.bodyLarge)

                    // Minutes input
                    OutlinedTextField(
                        value = minuteInput,
                        onValueChange = { input ->
                            if (input.length <= 2 && input.all { it.isDigit() }) {
                                minuteInput = input
                            }
                        },
                        label = { Text(text = "Mins") },
                        placeholder = { Text(text = "MM") },
                        singleLine = true,
                        isError = minuteInput.toIntOrNull() !in 0..59,
                        modifier = Modifier.width(80.dp)
                    )
                    AppText(text = " : ", style = MaterialTheme.typography.bodyLarge)

                    // Seconds input
                    OutlinedTextField(
                        value = secondInput,
                        onValueChange = { input ->
                            if (input.length <= 2 && input.all { it.isDigit() }) {
                                secondInput = input
                            }
                        },
                        label = { Text(text = "Secs") },
                        placeholder = { Text(text = "SS") },
                        singleLine = true,
                        isError = secondInput.toIntOrNull() !in 0..59,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        },
        dismissButton = {
            AppButton(
                onClick = onDismiss,
                text = "Cancel"
            )
        },
        confirmButton = {
            AppButton(
                onClick = {
                    val h = hourInput.toIntOrNull() ?: 0
                    val m = minuteInput.toIntOrNull() ?: 0
                    val s = secondInput.toIntOrNull() ?: 0
                    onSubmit(h, m, s)
                },
                // enabled = isInputValid,
                text = "OK"
            )
        },
    )
}

@Preview
@Composable
fun PreviewTimePickerDialog() {
    TimerPicker(
        hours = 12,
        minutes = 30,
        seconds = 45,
        onDismiss = { /* Preview dismiss action */ },
        onSubmit = { hours, minutes, seconds ->
            // Handle the submitted time in the preview
            println("Submitted time: $hours:$minutes:$seconds")
        }
    )
}

