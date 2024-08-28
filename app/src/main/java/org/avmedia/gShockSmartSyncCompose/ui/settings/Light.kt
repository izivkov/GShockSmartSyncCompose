package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextLarge
import AppTextVeryLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun Light(
    autoLightOn: Boolean,
    onAutoLightToggle: (Boolean) -> Unit,
    nightOnly: Boolean,
    onNightOnlyToggle: (Boolean) -> Unit,
    selectedLightDuration: String,
    onLightDurationChange: (String) -> Unit
) {
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
                    checked = autoLightOn,
                    onCheckedChange = onAutoLightToggle
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
                        selected = selectedLightDuration == "short",
                        onClick = { onLightDurationChange("short") },
                        modifier = Modifier.padding(end = 0.dp)
                    )
                    AppText(text = "2s")

                    RadioButton(
                        selected = selectedLightDuration == "long",
                        onClick = { onLightDurationChange("long") },
                        modifier = Modifier.padding(end = 0.dp)
                    )
                    AppText(text = "4s")
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
        onAutoLightToggle = { /* Handle toggle here */ },
        nightOnly = false,
        onNightOnlyToggle = { /* Handle toggle here */ },
        selectedLightDuration = "short",
        onLightDurationChange = { /* Handle change here */ }
    )
}

