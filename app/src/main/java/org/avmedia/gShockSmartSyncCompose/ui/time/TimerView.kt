package org.avmedia.gShockSmartSyncCompose.ui.time

import AppTextExtraLarge
import AppTextLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppButton
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun TimerView(
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit,
    timeModel: TimeModel = viewModel()
) {
    val timer by timeModel.timer.collectAsState()

    fun makeLongString(inSeconds: Int): String {
        val hours = inSeconds / 3600
        val minutesAndSeconds = inSeconds % 3600
        val minutes = minutesAndSeconds / 60
        val seconds = minutesAndSeconds % 60

        return "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
    }

    LaunchedEffect(timer) {
    }

    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
        //.clickable { /* Handle click if needed */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                AppTextLarge(
                    text = stringResource(R.string.timer),
                )
                TimerTimeView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    timeText = makeLongString(timer)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SendTimerButton(
                    modifier = Modifier
                        .padding(5.dp),
                    onClick = onSendClick
                )
            }
        }
    }
}

@Composable
fun TimerTimeView(modifier: Modifier = Modifier, timeText: String) {
    AppTextExtraLarge(
        text = timeText,
    )
}

@Composable
fun SendTimerButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppButton(
        text = stringResource(R.string.send_to_watch),
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerView() {
    TimerView(Modifier, onSendClick = {
        println("Timer Clicked")
    })
}