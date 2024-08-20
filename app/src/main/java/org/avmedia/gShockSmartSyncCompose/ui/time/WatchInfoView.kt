package org.avmedia.gShockSmartSyncCompose.ui.time

import AppText
import WatchTemperature
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.HorizontalSeparator
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton
import org.avmedia.gshockapi.WatchInfo

@Composable
fun WatchInfoView(modifier: Modifier) {
    Box(
        modifier = modifier.height(160.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            AppCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically), // Center the entire column vertically
                    verticalArrangement = Arrangement.SpaceBetween, // Space between the Row and HomeTime
                    horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
                ) {
                    Row(
                        modifier = Modifier.padding(0.dp, 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = stringResource(id = R.string.home_time),
                            modifier = Modifier
                        )

                        HorizontalSeparator(10.dp)

                        InfoButton(
                            infoText = stringResource(id = R.string.info_home_time),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(0.dp, 20.dp),// .weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HomeTime(
                            modifier = Modifier
                        )
                    }
                }
            }

            AppCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically), // Center the entire column vertically
                    verticalArrangement = Arrangement.SpaceBetween, // Space between the Row and HomeTime
                    horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
                ) {
                    Row(
                    ) {
                        BatteryView(
                            modifier = Modifier
                                .size(90.dp)
                                .rotate(90f)
                                .padding(vertical = 0.dp, horizontal = 30.dp),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(0.dp, 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WatchTemperature(
                            hasTemperature = WatchInfo.hasTemperature,
                            isNormalButtonPressed = true,
                            isConnected = true,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTwoCardsHorizontalLayout() {
    WatchInfoView(
        modifier = Modifier
    )
}
