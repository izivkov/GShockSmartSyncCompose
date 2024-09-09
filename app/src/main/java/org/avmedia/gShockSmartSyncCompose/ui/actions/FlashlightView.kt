package org.avmedia.gShockSmartSyncCompose.ui.actions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun FlashlightView(
    onUpdate: (ActionsViewModel.ToggleFlashlightAction) -> Unit = ActionsViewModel::updateAction,
    actionsViewModel: ActionsViewModel = viewModel()
) {
    val classType = ActionsViewModel.ToggleFlashlightAction::class.java
    val actions by actionsViewModel.actions.collectAsState()
    val flashlightAction: ActionsViewModel.ToggleFlashlightAction =
        actionsViewModel.getAction(classType)

    var isEnabled by remember { mutableStateOf(flashlightAction.enabled) }

    LaunchedEffect(actions, flashlightAction) {
        isEnabled = flashlightAction.enabled
    }

    ActionItem(
        title = stringResource(id = R.string.toggle_flashlight),
        resourceId = R.drawable.flashlight,
        isEnabled = isEnabled,
        onEnabledChange = { newValue ->
            isEnabled = newValue // Update the state when the switch is toggled
            flashlightAction.enabled = newValue
            onUpdate(flashlightAction.copy(enabled = newValue))
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFlashlight() {
    FlashlightView()
}

