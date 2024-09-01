package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun SetTimeView(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.set_time),
        resourceId = R.drawable.ic_watch_later_black_24dp,
        isEnabled = true,
        onEnabledChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSetTime() {
    SetTimeView(modifier = Modifier)
}

