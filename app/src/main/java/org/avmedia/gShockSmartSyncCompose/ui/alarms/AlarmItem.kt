package org.avmedia.gShockSmartSyncCompose.ui.alarms

import AppSwitch
import AppText
import AppTextExtraLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun AlarmItem(
    time: String = stringResource(id = R.string._12_00),
    isAlarmEnabled: Boolean = true,
    onToggleAlarm: (Boolean) -> Unit
) {
    var isEnabled by remember { mutableStateOf(isAlarmEnabled) }

    LaunchedEffect(isAlarmEnabled) {
        isEnabled = isAlarmEnabled
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextExtraLarge(
                    text = time,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp),
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between texts
                AppText(
                    text = stringResource(id = R.string.daily),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AppSwitch(
                checked = isEnabled,
                onCheckedChange = { checked ->
                    isEnabled = checked
                    onToggleAlarm(checked)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAlarmItem() {
    AlarmItem(onToggleAlarm = {})
}
