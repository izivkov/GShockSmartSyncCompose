package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun OperationalTone(
    isSwitchOn: Boolean,
) {
    val settings = SettingsModel.getButtonSound()
    var sound by remember { mutableStateOf(isSwitchOn) }

    val title = stringResource(id = R.string.operational_sound)
    BasicSettings(title = title,
        isSwitchOn = sound,
        onSwitchToggle = { newValue ->
            sound = newValue // Update the state when the switch is toggled
            settings.sound = newValue
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOperationalTone() {
    OperationalTone(isSwitchOn = true)
}