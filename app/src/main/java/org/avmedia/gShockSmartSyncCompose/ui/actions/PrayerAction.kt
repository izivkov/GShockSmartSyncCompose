package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun PrayerAlarms(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.set_prayer_alarms),
        resourceId = R.drawable.prayer_times,
        isEnabled = true,
        onEnabledChange = {},
        infoText = stringResource(id = R.string.prayer_times_info)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPrayerAction() {
    PrayerAlarms(modifier = Modifier)
}

