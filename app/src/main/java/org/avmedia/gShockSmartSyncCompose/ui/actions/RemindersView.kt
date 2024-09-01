package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun RemindersView(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.set_reminders),
        resourceId = R.drawable.events,
        isEnabled = true,
        onEnabledChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewReminders() {
    RemindersView(modifier = Modifier)
}

