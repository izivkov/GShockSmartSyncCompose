package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppText
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    onUpdate: (SettingsViewModel.TimeAdjustment) -> Unit = SettingsViewModel::updateSetting,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val classType = SettingsViewModel.TimeAdjustment::class.java
    val settings by settingsViewModel.settings.collectAsState()
    val timeAdjustmentSetting: SettingsViewModel.TimeAdjustment =
        settingsViewModel.getSetting(classType)

    var fineAdjustment by remember { mutableIntStateOf(timeAdjustmentSetting.fineAdjustment) }

    LaunchedEffect(settings) {
        fineAdjustment = timeAdjustmentSetting.fineAdjustment
    }

    var fineAdjustmentTextField by remember {
        mutableStateOf(TextFieldValue(fineAdjustment.toString()))
    }

    // Synchronize the TextField with `fineAdjustment` whenever it changes
    LaunchedEffect(fineAdjustment) {
        fineAdjustmentTextField = TextFieldValue(fineAdjustment.toString())
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            text = stringResource(R.string.fine_time_adjustment),
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 6.dp)
        )
        InfoButton(infoText = stringResource(R.string.fine_adjustment_info))

        Spacer(modifier = Modifier.weight(1f))

        NumericInputField(
            value = fineAdjustmentTextField,
            onValueChange = { newValue ->
                fineAdjustmentTextField = newValue
                val adjustmentValue = newValue.text.toIntOrNull()
                    ?: 0  // Fallback to 0 if the text is empty or invalid
                onUpdate(timeAdjustmentSetting.copy(fineAdjustment = adjustmentValue))
            },
            placeholderText = stringResource(R.string._0000),
            modifier = Modifier.width(IntrinsicSize.Min),
            range = -5000..5000,
            maxLength = 5,
        )
    }
}


