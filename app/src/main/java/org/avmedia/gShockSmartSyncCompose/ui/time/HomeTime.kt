package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gshockapi.GShockAPI
import org.avmedia.gshockapi.WatchInfo

@Composable
fun HomeTime(
    modifier: Modifier = Modifier,
    defaultText: String = "N/A"
) {
    var text by remember { mutableStateOf(defaultText) }

    LaunchedEffect(Unit) {
        if (api().isConnected() && api().isNormalButtonPressed()) {
            text = withContext(Dispatchers.IO) {
                if (WatchInfo.worldCities) api().getHomeTime() else defaultText
            }
        }
    }

    AppText(
        text = text,
        modifier = modifier
    )
}

@Composable
fun updateHomeTime(api: GShockAPI, onUpdate: (String) -> Unit) {
    LaunchedEffect(Unit) {
        val homeTime = withContext(Dispatchers.IO) {
            api.getHomeTime()
        }
        onUpdate(homeTime)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeTime() {
    HomeTime(Modifier, "America/Toronto")
}
