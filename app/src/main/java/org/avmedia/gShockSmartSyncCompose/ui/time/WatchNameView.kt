package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api

@Composable
fun WatchNameView(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<String>("Loading...") }

    // Launch the coroutine to call the suspend function
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val data = api().getWatchName()
            result = data
        }
    }

    AppCard(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxHeight()) {
            WatchName(
                modifier = Modifier
                    .align(Alignment.Center) // Aligns the text in the center of the Box (both horizontally and vertically)
                    .fillMaxWidth(),
                text = result
            )
        }
    }
}

@Composable
fun WatchName(
    modifier: Modifier = Modifier,
    text: String
) {
    AppText(
        text = text,
        fontSize = 48.sp,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWatchName() {
    WatchNameView(Modifier)
}

