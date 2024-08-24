package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonData
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonsRow
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenContainer
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle

@Composable
fun SettingsScreen(navController: NavController) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScreenContainer()
            {
                ScreenTitle(stringResource(id = R.string.settings), Modifier)
                SettingsList()
            }
        }
    }
}

@Composable
fun SettingsList() {

    @Composable
    fun createSettings(): List<Any> {
        val settings = listOf(
            Locale(
                dateFormat = "MM:DD",
                timeFormat = "12h",
                selectedLanguage = "Spanish",
                onDateFormatChange = {},
                onTimeFormatChange = {},
                onLanguageChange = {}),

            OperationalTone(
                isSwitchOn = true,
                onSwitchToggle = {}),

            Light(
                autoLightOn = true,
                onAutoLightToggle = { /* Handle toggle here */ },
                nightOnly = false,
                onNightOnlyToggle = { /* Handle toggle here */ },
                selectedLightDuration = "short",
                onLightDurationChange = { /* Handle change here */ }),

            PowerSavings(
                isSwitchOn = true,
                onSwitchToggle = {}),

            TimeAdjustment(
                timeAdjustmentOnOffChecked = true,
                notifyMeChecked = false,
                adjustmentMinutes = "30",
                onTimeAdjustmentSwitchToggle = {},
                onNotifyMeCheckedChange = {},
                onAdjustmentMinutesChange = {}),
        )

        return settings
    }

    Column(
        modifier = Modifier
    ) {
        ItemList(createSettings())

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start=26.dp),
                verticalAlignment = Alignment.CenterVertically,  // Center vertically
                horizontalArrangement = Arrangement.SpaceEvenly,  // Arrange horizontally, starting from the left
            ) {
                InfoButton(infoText = stringResource(id = R.string.auto_fill_help))

                val buttons = arrayListOf(
                    ButtonData(
                        text = stringResource(id = R.string.auto_configure_settings),
                        onClick = { println("Auto-fill settings values") }),
                    ButtonData(
                        text = stringResource(id = R.string.send_events_to_watch),
                        onClick = { println("Send alarms to phone clicked") })
                )

                ButtonsRow(buttons = buttons)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(NavController(LocalContext.current))
}
