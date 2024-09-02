package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun PrayerAlarmsView(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(id = R.string.set_prayer_alarms)
    val action = ActionsModel.actionMap[title] as ActionsModel.PrayerAlarmsAction
    var isEnabled by remember { mutableStateOf(action.enabled) }
    val context = LocalContext.current

    ActionItem(
        title = title,
        resourceId = R.drawable.prayer_times,
        isEnabled = isEnabled,
        onEnabledChange = { newValue ->
            isEnabled = newValue // Update the state when the switch is toggled
            action.enabled = newValue
            action.save(context)
        },
        infoText = stringResource(id = R.string.prayer_times_info)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPrayerAction() {
    PrayerAlarmsView(modifier = Modifier)
}

