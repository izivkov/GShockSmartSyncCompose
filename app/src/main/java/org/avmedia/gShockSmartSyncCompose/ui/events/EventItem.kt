package org.avmedia.gShockSmartSyncCompose.ui.events

import AppText
import AppTextLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@Composable
fun EventItem(
    title: String,
    period: String,
    frequency: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    AppText(
                        text = title,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp)
                    )
                    Switch(
                        checked = enabled,
                        onCheckedChange = onEnabledChange,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                ) {
                    val (periodRef, frequencyRef) = createRefs()

                    AppText(
                        text = period,
                        modifier = Modifier.padding(start = 6.dp).constrainAs(periodRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                    )

                    AppText(
                        text = frequency,
                        modifier = Modifier.constrainAs(frequencyRef) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAlarmScreen() {
    EventItem(
        title = "Sample Event",
        period = "Feb 22 - Mar 24",
        frequency = "Weekly",
        enabled = false,
        onEnabledChange = {}
    )
}
