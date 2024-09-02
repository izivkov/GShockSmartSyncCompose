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
fun VoiceAssistView(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(id = R.string.start_voice_assistant)
    val action = ActionsModel.actionMap[title] as ActionsModel.StartVoiceAssistAction
    var isEnabled by remember { mutableStateOf(action.enabled) }
    val context = LocalContext.current

    ActionItem(
        title = title,
        resourceId = R.drawable.voice_assist,
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
fun PreviewVoiceAssist() {
    VoiceAssistView(modifier = Modifier)
}

