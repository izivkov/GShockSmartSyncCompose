package org.avmedia.gShockSmartSyncCompose.ui.alarms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppButton
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    onSendAlarmsToPhoneClick: () -> Unit,
    onSendAlarmsToWatchClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppButton(
            text = stringResource(id = R.string.send_alarms_to_phone),
            onClick = onSendAlarmsToPhoneClick,
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        AppButton(
            text = stringResource(id = R.string.send_alarms_to_watch),
            onClick = onSendAlarmsToWatchClick,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
        )

        InfoButton(
            infoText = stringResource(id = R.string.alarms_screen_info),
        )
        Box(modifier = Modifier.width(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonRow() {
    ButtonsRow(
        modifier = Modifier
            .padding(4.dp),
        onSendAlarmsToPhoneClick = {
            // Handle send alarms to phone button click
            println("Send alarms to phone clicked")
        },
        onSendAlarmsToWatchClick = {
            // Handle send alarms to watch button click
            println("Send alarms to watch clicked")
        },
        onInfoClick = {
            // Handle info button click
            println("Info button clicked")
        }
    )
}
