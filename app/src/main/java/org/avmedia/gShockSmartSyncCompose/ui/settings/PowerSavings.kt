package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun PowerSavings(
    isSwitchOn: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    val title = stringResource(id = R.string.power_saving_mode)
    BasicSettings(title = title, isSwitchOn = isSwitchOn, onSwitchToggle)
}

@Preview(showBackground = true)
@Composable
fun PreviewPowerSavings() {
    PowerSavings(
        isSwitchOn = true,
        onSwitchToggle = {})
}