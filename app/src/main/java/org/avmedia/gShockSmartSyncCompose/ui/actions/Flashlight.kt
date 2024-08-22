package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun Flashlight(
    modifier: Modifier = Modifier,
) {
    ActionItem(
        title = stringResource(id = R.string.toggle_flashlight),
        resourceId = R.drawable.flashlight,
        isEnabled = true,
        onEnabledChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFlashlight() {
    Flashlight(modifier = Modifier)
}

