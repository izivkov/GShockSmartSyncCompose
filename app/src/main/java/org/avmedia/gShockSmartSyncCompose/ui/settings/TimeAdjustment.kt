package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextLarge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton

@Composable
fun TimeAdjustment(
    timeAdjustmentOnOffChecked: Boolean,
    notifyMeChecked: Boolean,
    adjustmentMinutes: String,
    onTimeAdjustmentSwitchToggle: (Boolean) -> Unit,
    onNotifyMeCheckedChange: (Boolean) -> Unit,
    onAdjustmentMinutesChange: (String) -> Unit
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTextLarge(
                    text = stringResource(id = R.string.time_adjustment),
                    modifier = Modifier.padding(end = 6.dp)
                )
                InfoButton(infoText = stringResource(id = R.string.time_adjustment_info))
                Spacer(modifier = Modifier.weight(1f))
                AppSwitch(
                    checked = timeAdjustmentOnOffChecked,
                    onCheckedChange = onTimeAdjustmentSwitchToggle,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = stringResource(id = R.string.adjustment_time_minutes),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                InfoButton(infoText = stringResource(id = R.string.adjustment_time_info))

                Spacer(modifier = Modifier.weight(1f))

                OutlinedTextField(
                    value = adjustmentMinutes,
                    onValueChange = onAdjustmentMinutesChange,
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                        .weight(1.0f),
                    placeholder = {
                        AppText(text = stringResource(id = R.string._00))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.End
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = stringResource(id = R.string.notify_me),
                    modifier = Modifier.wrapContentWidth(),
                )
                Checkbox(
                    checked = notifyMeChecked,
                    onCheckedChange = onNotifyMeCheckedChange
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeAdjustment() {
    TimeAdjustment(
        timeAdjustmentOnOffChecked = true,
        notifyMeChecked = false,
        adjustmentMinutes = "15",
        onTimeAdjustmentSwitchToggle = {},
        onNotifyMeCheckedChange = {},
        onAdjustmentMinutesChange = {}
    )
}


