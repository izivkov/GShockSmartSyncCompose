package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun SkipToNextTrackView(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.next_track),
        resourceId = R.drawable.skip_next,
        infoText = stringResource(id = R.string.skip_to_next_track_info),
        isEnabled = true,
        onEnabledChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSkipToNextTrack() {
    SkipToNextTrackView(modifier = Modifier)
}

