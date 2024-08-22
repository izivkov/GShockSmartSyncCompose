package org.avmedia.gShockSmartSyncCompose.ui.actions

import PhoneCall
import Photo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.alarms.AlarmsScreen
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenContainer
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle

@Composable
fun ActionsScreen(navController: NavController) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScreenContainer()
            {
                ScreenTitle(stringResource(id = R.string.actions), Modifier)
                ActionList()
            }
        }
    }
}

@Composable
fun ActionList() {

    @Composable
    fun createActions(): List<Any> {
        val actions = listOf(
            SetTime(),
            Reminders(),
            Photo(modifier = Modifier, onActionEnabledChange = {}, onOrientationChange = {}),
            Flashlight(),
            VoiceAssist(),
            SkipToNextTrack(),
            PrayerAlarms(),
            Separator(modifier = Modifier),
            PhoneCall(
                modifier = Modifier,
                onPhoneNumberChange = {},
                isActionEnabled = true,
                onActionEnabledChange = {}),
        )

        return actions
    }

    Column(
        modifier = Modifier
    ) {
        ItemList(createActions())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionsScreen() {
    ActionsScreen(NavController(LocalContext.current))
}

