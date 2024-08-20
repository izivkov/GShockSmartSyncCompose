package org.avmedia.gShockSmartSyncCompose.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.alarms.AlarmsScreen
import org.avmedia.gShockSmartSyncCompose.ui.alarms.ButtonData
import org.avmedia.gShockSmartSyncCompose.ui.alarms.ButtonsRow
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemView
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenContainer
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle
import org.avmedia.gshockapi.Event

@Composable
fun EventsScreen(navController: NavController) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScreenContainer()
            {
                ScreenTitle(stringResource(id = R.string.events), Modifier)

                EventList()

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                ) {

                    val buttons = arrayListOf(
                        ButtonData(text = stringResource(id = R.string.send_events_to_watch), onClick = { println("Send alarms to phone clicked") })
                    )

                    ButtonsRow(buttons = buttons)

                }
            }
        }
    }
}

@Composable
fun EventList(eventViewModel: EventViewModel = viewModel()) {
    val events by eventViewModel.events.collectAsState()

    @Composable
    fun createEvent(): List<Any> {
        val eventItems = mutableListOf<Any>()

        events.forEachIndexed { index: Int, event:Event ->
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