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
fun BasicSettings(
    title: String,
    isSwitchOn: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    // Custom CardView with stroke and elevation
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                AppTextVeryLarge(
                    text = title,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }

            // Switch to toggle the option
            AppSwitch(
                checked = isSwitchOn,
                onCheckedChange = onSwitchToggle
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBasicSettings() {
    BasicSettings(
        title = "Basic Settings",
        isSwitchOn = true,
        onSwitchToggle = {})
}