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
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.R

@Composable
fun SetTimeView(
    modifier: Modifier = Modifier,
) {
    val action = ActionsModel.getSetTimeAction()
    var isEnabled by remember { mutableStateOf(action.enabled) }
    val context = LocalContext.current

    ActionItem(
        title = stringResource(id = R.string.set_time),
        resourceId = R.drawable.ic_watch_later_black_24dp,
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
fun PreviewSetTime() {
    SetTimeView(modifier = Modifier)
}

