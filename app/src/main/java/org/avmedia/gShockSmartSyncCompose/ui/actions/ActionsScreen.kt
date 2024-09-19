package org.avmedia.gShockSmartSyncCompose.ui.actions

import PhoneView
import PhotoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle
import org.avmedia.gshockapi.WatchInfo
import kotlin.reflect.KFunction

@Composable
fun ActionsScreen(navController: NavController) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (title, actions) = createRefs()

                ScreenTitle(stringResource(id = R.string.actions), Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)  // Link top of content to parent top
                        bottom.linkTo(actions.top)  // Link bottom of content to top of buttonsRow
                    })

                Column(
                    modifier = Modifier
                        .constrainAs(actions) {
                            top.linkTo(title.bottom)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                        .verticalScroll(rememberScrollState())  // Make content scrollable
                        .padding(0.dp)
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    ActionList()
                }
            }
        }
    }
}

@Composable
fun ActionList() {

    @Composable
    fun createActions(): List<Any> {
        return listOfNotNull(
            // PhoneFinderView().takeIf { WatchInfo.findButtonUserDefined },
            if (WatchInfo.findButtonUserDefined) PhoneFinderView() else null,
            SetTimeView(),
            // RemindersView().takeIf { WatchInfo.hasReminders },
            if (WatchInfo.hasReminders) RemindersView() else null,
            PhotoView(),
            FlashlightView(),
            VoiceAssistView(),
            SkipToNextTrackView(),
            PrayerAlarmsView(),
            SeparatorView(),
            PhoneView(),
        )
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

