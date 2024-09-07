package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun PowerSavings(
    isSwitchOn: Boolean,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val setting = settingsViewModel.getSetting(SettingsViewModel.PowerSavingMode::class.java)
    var powerSavingMode by remember { mutableStateOf(isSwitchOn) }

    val title = stringResource(id = R.string.power_saving_mode)
    BasicSettings(title = title, isSwitchOn = powerSavingMode,
        onSwitchToggle = { newValue ->
            powerSavingMode = newValue // Update the state when the switch is toggled
            setting.powerSavingMode = newValue
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPowerSavings() {
    PowerSavings(isSwitchOn = true)
}