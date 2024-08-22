package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun VoiceAssist(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.voice_assistant),
        resourceId = R.drawable.voice_assist,
        isEnabled = true,
        onEnabledChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewVoiceAssist() {
    VoiceAssist(modifier = Modifier)
}

