package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun WatchNameView(
    modifier: Modifier = Modifier,
    watchName: String
) {
    AppCard(
        modifier = modifier
    ) {

        Box(modifier = Modifier.fillMaxHeight()) {
            WatchName(
                modifier = Modifier
                    .align(Alignment.Center) // Aligns the text in the center of the Box (both horizontally and vertically)
                    .fillMaxWidth(),
                text = watchName
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
    WatchNameView(Modifier, "Casio GW-5600")
}

