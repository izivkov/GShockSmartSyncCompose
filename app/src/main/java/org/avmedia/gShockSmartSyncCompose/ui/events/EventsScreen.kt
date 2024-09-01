package org.avmedia.gShockSmartSyncCompose.ui.events

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonData
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonsRow
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemView
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle
import org.avmedia.gShockSmartSyncCompose.utils.Utils.Companion.AppHashCode
import org.avmedia.gshockapi.Event
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents

@Composable
fun EventsScreen(navController: NavController) {

    RequestPermissions()

    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (title, events, buttonsRow) = createRefs()

                ScreenTitle(stringResource(id = R.string.events), Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(events.top)
                    })

                Column(
                    modifier = Modifier
                        .constrainAs(events) {
                            top.linkTo(title.bottom)
                            bottom.linkTo(buttonsRow.top)
                            height = Dimension.fillToConstraints
                        }
                        .verticalScroll(rememberScrollState())  // Make content scrollable
                        .padding(0.dp)
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    EventList()
                }

                Column(modifier = Modifier
                    .constrainAs(buttonsRow) {
                        top.linkTo(events.bottom)  // Link top of buttonsRow to bottom of content
                        bottom.linkTo(parent.bottom)  // Keep buttons at the bottom
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                ) {

                    val buttons = arrayListOf(
                        ButtonData(
                            text = stringResource(id = R.string.send_events_to_watch),
                            onClick = { println("Send alarms to phone clicked") })
                    )
                    ButtonsRow(buttons = buttons)
                }
            }
        }
    }
}

@Composable
fun RequestPermissions() {
    val requestMultiplePermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if all permissions are granted
        if (permissions.all { it.value }) {
            ProgressEvents.onNext("CalendarPermissionsGranted")
        } else {
            ProgressEvents.onNext("CalendarPermissionsNotGranted")
        }
    }

    // Trigger permission request
    LaunchedEffect(Unit) {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.READ_CALENDAR,
            )
        )
    }
}


@Composable
fun EventList() {
    val eventViewModel: EventViewModel = viewModel()
    val events by eventViewModel.events.collectAsState()

    fun waitForPermissions(context: Context) {
        val eventActions = arrayOf(
            EventAction("CalendarPermissionsGranted") {
                if (EventsModel.events.isEmpty()) {
                    eventViewModel.loadEvents(context)
                }
            },
        )

        ProgressEvents.runEventActions(AppHashCode(), eventActions)
    }
    waitForPermissions(LocalContext.current)

    @Composable
    fun createEvent(): List<Any> {
        val eventItems = mutableListOf<Any>()
        events.forEachIndexed { index: Int, event: Event ->
            ItemView {
                EventItem(
                    title = event.title,
                    period = event.getPeriodFormatted(),
                    frequency = event.getFrequencyFormatted(),
                    enabled = event.enabled,
                    onEnabledChange = {}
                )
            }
        }

        return eventItems.toList()
    }

    Column(
        modifier = Modifier
    ) {
        ItemList(createEvent())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEventsScreen() {
    EventsScreen(NavController(LocalContext.current))
}