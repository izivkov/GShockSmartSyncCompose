package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppText
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton
import org.avmedia.gShockSmartSyncCompose.ui.common.NumericInputField

@Composable
fun FineAdjustmentRow(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val classType = SettingsViewModel.TimeAdjustment::class.java
    val timeAdjustmentSetting: SettingsViewModel.TimeAdjustment =
        settingsViewModel.getSetting(classType)

    val currentValue = timeAdjustmentSetting.fineAdjustment

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            text = stringResource(R.string.fine_time_adjustment),
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 6.dp)
        )
        InfoButton(infoText = "When setting time, add this value to the current time.")

        Spacer(modifier = Modifier.weight(1f))

        var fineAdjustment by remember {
            mutableStateOf(
                TextFieldValue(
                    currentValue.toString()
                        // .padStart(4, '0')
                )
            )
        }

        NumericInputField(
            value = fineAdjustment,
            onValueChange = { newValue ->
                fineAdjustment = newValue
                // Additional logic, if needed
            },
            placeholderText = stringResource(R.string._0000),
            modifier = Modifier.width(IntrinsicSize.Min),
            range = -5000..5000,
            maxLength = 5,
            )
    }
}


