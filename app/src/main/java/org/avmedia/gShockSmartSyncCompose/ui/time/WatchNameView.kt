package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun WatchNameView(
    modifier: Modifier = Modifier,
    watchName: String
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        WatchName(
            modifier = Modifier
                .fillMaxWidth(),
            text = watchName
        )
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
            .padding(0.dp, 40.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWatchName() {
    WatchNameView(Modifier, "Casio GW-5600")
}

