package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import AppTextExtraLarge
import AppTextLarge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppButton
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun LocalTimeView(modifier: Modifier) {
    AppCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First Column: Local Time and TimeZoneTextView
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(6.dp)
            ) {
                AppTextLarge(
                    modifier = Modifier.padding(start = 6.dp),
                    text = stringResource(id = R.string.local_time),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                TextClockComposable(
                    modifier = Modifier.align(Alignment.Start)
                )

                Box {
                    TimeZoneTextView(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 6.dp),
                        textSize = 16.sp
                    )
                }
            }

            // Second Column: Send Time Button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SendTimeButton(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    text = stringResource(id = R.string.send_to_watch)
                )
            }
        }
    }
}

@Composable
fun TextClockComposable(modifier: Modifier = Modifier) {
    // Custom implementation for TextClock
    // This is where you might implement a Composable that mimics the TextClock's behavior
    // using remember, LaunchedEffect, etc., to keep time updated.
    AppTextExtraLarge(
        text = "12:00:00 PM", // Placeholder, replace with actual clock logic
        modifier = modifier
    )
}

@Composable
fun TimeZoneTextView(modifier: Modifier = Modifier, textSize: TextUnit) {
    // Custom implementation for TimeZoneTextView
    AppText(
        text = "America/Toronto", // Placeholder, replace with actual timezone logic
        fontSize = textSize,
        modifier = modifier
    )
}

@Composable
fun SendTimeButton(modifier: Modifier = Modifier, text: String) {
    AppButton(
        onClick = { /* TODO: Implement button logic */ },
        text = text
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLocalTimeCard() {
    LocalTimeView(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 0.dp) // Adjust padding as needed
    )
}