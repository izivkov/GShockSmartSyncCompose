package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppTextVeryLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

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