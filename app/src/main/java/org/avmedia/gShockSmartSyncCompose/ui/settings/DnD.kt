package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppSwitch
import AppText
import AppTextLarge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gshockapi.WatchInfo

@Composable
fun DnD(
    onUpdate: (SettingsViewModel.DnD) -> Unit = SettingsViewModel::updateSetting,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val classType = SettingsViewModel.DnD::class.java

    val settings by settingsViewModel.settings.collectAsState()
    val dndSetting: SettingsViewModel.DnD = settingsViewModel.getSetting(classType)

    var dnd by remember { mutableStateOf(dndSetting.dnd) }
    var mirrorPhone by remember { mutableStateOf(dndSetting.mirrorPhone) }

    LaunchedEffect(settings, dnd, mirrorPhone) {
        dnd = dndSetting.dnd
        mirrorPhone = dndSetting.mirrorPhone
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
        ) {
            // Auto Light Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    AppTextLarge(
                        text = stringResource(id = R.string.do_not_disturb_dnd),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
                AppSwitch(
                    checked = dnd,
                    enabled = !mirrorPhone,
                    onCheckedChange = {
                        dnd = it
                        dndSetting.dnd = it
                        onUpdate(dndSetting.copy(dnd = it))
                    }
                )
            }

            if (WatchInfo.alwaysConnected) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.mirror_phone_s_dnd),
                        modifier = Modifier.wrapContentWidth(),
                    )
                    Checkbox(
                        checked = mirrorPhone,
                        onCheckedChange = { newValue ->
                            mirrorPhone = newValue // Update the state when the switch is toggled
                            dndSetting.mirrorPhone = newValue
                            onUpdate(dndSetting.copy(mirrorPhone = mirrorPhone))
                        }
                    )
                }
            }
        }
    }
}

