package org.avmedia.gShockSmartSyncCompose.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import org.avmedia.gShockSmartSyncCompose.ui.actions.FlashlightView
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonData
import org.avmedia.gShockSmartSyncCompose.ui.common.ButtonsRow
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton
import org.avmedia.gShockSmartSyncCompose.ui.common.ItemList
import org.avmedia.gShockSmartSyncCompose.ui.common.ScreenTitle

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    GShockSmartSyncTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (title, settingsLayout, buttonsRow) = createRefs()

                ScreenTitle(stringResource(id = R.string.settings), Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)  // Link top of content to parent top
                        bottom.linkTo(settingsLayout.top)  // Link bottom of content to top of buttonsRow
                    })

                Column(
                    modifier = Modifier
                        .constrainAs(settingsLayout) {
                            top.linkTo(title.bottom)
                            bottom.linkTo(buttonsRow.top)
                            height = Dimension.fillToConstraints
                        }
                        .verticalScroll(rememberScrollState())  // Make content scrollable
                        .padding(0.dp)
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {
                    SettingsList()
                }

                BottomRow(modifier = Modifier.constrainAs(buttonsRow) {
                    top.linkTo(settingsLayout.bottom)  // Link top of buttonsRow to bottom of content
                    bottom.linkTo(parent.bottom)  // Keep buttons at the bottom
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                )
            }
        }
    }
}

@Composable
fun SettingsList() {

    val settingsViews = arrayListOf (
        Locale(onTimeFormatChange = {}, onDateFormatChange = {}, onLanguageChange = {}, onSettingChanged = {}),
        OperationalTone(),
        Light(onSettingChanged = {}),
        PowerSavings(),

        TimeAdjustment()
    )

    Column(
        modifier = Modifier
    ) {
        ItemList(settingsViews)
    }
}

@Composable
fun BottomRow(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 26.dp),
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

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(NavController(LocalContext.current))
}
