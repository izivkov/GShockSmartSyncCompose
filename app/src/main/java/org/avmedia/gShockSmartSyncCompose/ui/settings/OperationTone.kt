package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun OperationalTone(
    isSwitchOn: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    val title = stringResource(id = R.string.operational_sound)
    BasicSettings(title = title, isSwitchOn = isSwitchOn, onSwitchToggle)
}

@Preview(showBackground = true)
@Composable
fun PreviewOperationalTone() {
    OperationalTone(
        isSwitchOn = true,
        onSwitchToggle = {})
}