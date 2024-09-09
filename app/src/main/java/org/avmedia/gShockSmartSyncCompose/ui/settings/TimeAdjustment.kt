package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextLarge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton

@Composable
fun TimeAdjustment(
    onUpdate: (SettingsViewModel.TimeAdjustment) -> Unit = SettingsViewModel::updateSetting,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val classType = SettingsViewModel.TimeAdjustment::class.java

    val settings by settingsViewModel.settings.collectAsState()
    val timeAdjustmentSetting: SettingsViewModel.TimeAdjustment =
        settingsViewModel.getSetting(classType)

    var timeAdjustment by remember { mutableStateOf(timeAdjustmentSetting.timeAdjustment) }

    var notifyMe by remember { mutableStateOf(timeAdjustmentSetting.timeAdjustmentNotifications) }
    var adjustmentMinutes by remember { mutableStateOf(timeAdjustmentSetting.adjustmentTimeMinutes.toString()) }

    LaunchedEffect(settings, timeAdjustment, notifyMe, adjustmentMinutes) {
        timeAdjustment = timeAdjustmentSetting.timeAdjustment
        notifyMe = timeAdjustmentSetting.timeAdjustmentNotifications
        adjustmentMinutes = timeAdjustmentSetting.adjustmentTimeMinutes.toString()
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextLarge(
                    text = stringResource(id = R.string.time_adjustment),
                    modifier = Modifier.padding(end = 6.dp)
                )
                InfoButton(infoText = stringResource(id = R.string.time_adjustment_info))
                Spacer(modifier = Modifier.weight(1f))
                AppSwitch(
                    checked = timeAdjustment,
                    onCheckedChange = { newValue ->
                        timeAdjustment = newValue // Update the state when the switch is toggled
                        timeAdjustmentSetting.timeAdjustment = newValue
                        onUpdate(timeAdjustmentSetting.copy(timeAdjustment = newValue))
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = stringResource(id = R.string.adjustment_time_minutes),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                InfoButton(infoText = stringResource(id = R.string.adjustment_time_info))

                Spacer(modifier = Modifier.weight(1f))

                OutlinedTextField(
                    value = adjustmentMinutes,
                    onValueChange = { newValue ->
                        // Validate that the input is numeric before updating the state
                        if (newValue.all { it.isDigit() }) {
                            adjustmentMinutes = newValue // Update the text field
                            timeAdjustmentSetting.adjustmentTimeMinutes =
                                newValue.toIntOrNull() ?: 0 // Update the model safely
                            onUpdate(
                                timeAdjustmentSetting.copy(
                                    adjustmentTimeMinutes = newValue.toIntOrNull() ?: 0
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                        .weight(1.0f),
                    placeholder = {
                        AppText(text = stringResource(id = R.string._00))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.End
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = stringResource(id = R.string.notify_me),
                    modifier = Modifier.wrapContentWidth(),
                )
                Checkbox(
                    checked = notifyMe,
                    onCheckedChange = { newValue ->
                        notifyMe = newValue // Update the state when the switch is toggled
                        timeAdjustmentSetting.timeAdjustmentNotifications = newValue
                        onUpdate(timeAdjustmentSetting.copy(timeAdjustmentNotifications = newValue))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeAdjustment() {
    TimeAdjustment()
}


