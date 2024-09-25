package org.avmedia.gShockSmartSyncCompose.ui.others

import AppTextLarge
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents

@Composable
fun WatchName(modifier: Modifier) {
    val noWatchString = stringResource(id = R.string.no_watch)
    var textValue by remember { mutableStateOf(noWatchString) }

    // Subscribe to ProgressEvents and update the text value
    LaunchedEffect(Unit) {
        textValue =
            LocalDataStorage.get(applicationContext(), "LastDeviceName", "No Watch") ?: "No Watch"

        createSubscription { updatedText ->
            textValue = if (updatedText.isBlank()) {
                noWatchString // Use pre-fetched string resource here
            } else {
                updatedText.removePrefix("CASIO").trim()
            }
        }
    }

    // Display the text that gets updated from ProgressEvents
    AppTextLarge(text = textValue, modifier = modifier)
}

@SuppressLint("SetTextI18n")
private fun createSubscription(onTextUpdated: (String) -> Unit) {
    val eventActions = arrayOf(
        EventAction("DeviceName") {
            val deviceName = ProgressEvents.getPayload("DeviceName") as String
            onTextUpdated(deviceName)
        }
    )

    CoroutineScope(Dispatchers.IO).launch {
        ProgressEvents.runEventActions("ProgressEventText", eventActions)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWatchName() {
    WatchName(
        modifier = Modifier
            .padding(start = 0.dp)
    )
}
