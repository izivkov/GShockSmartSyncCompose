package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun SkipToNextTrackView(
    modifier: Modifier = Modifier,
    actionsViewModel: ActionsViewModel = viewModel(),
) {
    val classType = ActionsViewModel.NextTrack::class.java

    var action = actionsViewModel.getAction(classType)
    val currentAction by remember { mutableStateOf(action) }

    LaunchedEffect(action) {
        snapshotFlow { actionsViewModel.getAction(classType) }
            .collect { newAction ->
                action = currentAction
            }
    }

    var isEnabled by remember { mutableStateOf(action.enabled) }
    val context = LocalContext.current

    ActionItem(
        title = stringResource(id = R.string.next_track),
        resourceId = R.drawable.skip_next,
        infoText = stringResource(id = R.string.skip_to_next_track_info),
        isEnabled = isEnabled,
        onEnabledChange = { newValue ->
            isEnabled = newValue // Update the state when the switch is toggled
            action.enabled = newValue
            action.save(context)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSkipToNextTrack() {
    SkipToNextTrackView(modifier = Modifier)
}

