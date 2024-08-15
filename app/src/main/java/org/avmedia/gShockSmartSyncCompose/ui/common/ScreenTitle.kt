package org.avmedia.gShockSmartSyncCompose.ui.common

import AppText
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenTitle(
    text: String,
) {
    AppText(
        text = text,
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        textAlign = TextAlign.Center
    )
}
