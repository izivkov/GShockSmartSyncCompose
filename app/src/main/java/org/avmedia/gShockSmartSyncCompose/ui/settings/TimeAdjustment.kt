package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextClickable
import AppTextLarge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton
import org.avmedia.gShockSmartSyncCompose.ui.common.NumericInputField
import org.avmedia.gShockSmartSyncCompose.ui.time.TimerPicker
import org.avmedia.gShockSmartSyncCompose.ui.time.convertSecondsToTime

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
    var fineAdjustment by remember { mutableStateOf(timeAdjustmentSetting.fineAdjustment.toString()) }

    LaunchedEffect(settings, timeAdjustment, notifyMe, adjustmentMinutes) {
        timeAdjustment = timeAdjustmentSetting.timeAdjustment
        notifyMe = timeAdjustmentSetting.timeAdjustmentNotifications
        adjustmentMinutes = timeAdjustmentSetting.adjustmentTimeMinutes.toString()
        fineAdjustment = timeAdjustmentSetting.fineAdjustment.toString()
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
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = stringResource(id = R.string.adjustment_time_minutes),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                InfoButton(infoText = stringResource(id = R.string.adjustment_time_info))

                Spacer(modifier = Modifier.weight(1f))

                var minutesInput by remember {
                    mutableStateOf(
                        TextFieldValue(
                            adjustmentMinutes.toString()
                        )
                    )
                }

                NumericInputField(
                    value = minutesInput,
                    onValueChange = { newValue ->
                        minutesInput = newValue
                        // Additional logic, if needed
                    },
                    placeholderText = stringResource(id = R.string._00),
                    modifier = Modifier.width(IntrinsicSize.Min),
                    range = -0..59,
                    maxLength = 2,
                )
            }

            FineAdjustmentRow(modifier = Modifier
                .padding(end = 12.dp, start = 12.dp, top = 6.dp)
            )

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


