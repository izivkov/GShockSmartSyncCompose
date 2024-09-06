package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gshockapi.WatchInfo

@Composable
fun Light(
    autoLightOn: Boolean,
    nightOnly: Boolean,
    selectedLightDuration: String,
    onSettingChanged: (SettingsModel.Setting) -> Unit
) {
    val settings = SettingsModel.getLight()
    var autoLight by remember { mutableStateOf(autoLightOn) }
    var night by remember { mutableStateOf(nightOnly) }
    var lightDuration by remember { mutableStateOf(selectedLightDuration) }

    LaunchedEffect(autoLightOn, nightOnly, selectedLightDuration) {
        autoLight = autoLightOn
        night = nightOnly
        lightDuration = selectedLightDuration
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
        ) {
            // Auto Light Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    AppTextLarge(
                        text = stringResource(id = R.string.auto_light),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
                AppSwitch(
                    checked = autoLight,
                    onCheckedChange = {
                        autoLight = it
                        settings.autoLight = it
                        onSettingChanged(settings)
                    }
                )
            }

            // Night Only Auto Light Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }

            // Illumination Period Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppTextLarge(
                    text = "Illumination Period",
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = lightDuration == SettingsModel.Light.LIGHT_DURATION.TWO_SECONDS.value,
                        onClick = {
                            lightDuration = SettingsModel.Light.LIGHT_DURATION.TWO_SECONDS.value
                            onSettingChanged(settings)
                        },
                        modifier = Modifier.padding(end = 0.dp)
                    )
                    AppText(text = WatchInfo.shortLightDuration)

                    RadioButton(
                        selected = lightDuration == SettingsModel.Light.LIGHT_DURATION.FOUR_SECONDS.value,
                        onClick = {
                            lightDuration = SettingsModel.Light.LIGHT_DURATION.FOUR_SECONDS.value
                            onSettingChanged(settings)
                        },
                        modifier = Modifier.padding(end = 0.dp)
                    )
                    AppText(text = WatchInfo.longLightDuration)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "SettingsItem Preview")
@Composable
fun PreviewSettingsItem() {
    // Providing mock data for the preview
    Light(
        autoLightOn = true,
        nightOnly = false,
        selectedLightDuration = "2s",
        onSettingChanged = { updatedSetting ->
            // Automatically updates the model when the setting is changed in the UI
            SettingsModel.settingsMap.toMutableMap()[updatedSetting.title] = updatedSetting
        }
    )
}

