package org.avmedia.gShockSmartSyncCompose.ui.actions

import AppText
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents

@Composable
fun ActionRunner(
    context: Context,
) {
    var isVisible by remember { mutableStateOf(false) }

    // Initialize and subscribe to events
    LaunchedEffect(Unit) {
        val eventActions = arrayOf(
            EventAction("RunActions") {
                // ActionsModel.loadData(context)
                ActionsViewModel.runActionsForActionButton(context)
            },
            EventAction("ButtonPressedInfoReceived") {
                // ActionsModel.loadData(context)

                when {
                    api().isActionButtonPressed() -> {
                        isVisible = true
                        ActionsViewModel.runActionsForActionButton(context)
                    }

                    api().isAutoTimeStarted() -> {
                        ActionsViewModel.runActionsForAutoTimeSetting(context)
                    }

                    api().isFindPhoneButtonPressed() -> {
                        isVisible = true
                        ActionsViewModel.runActionFindPhone(context)
                    }

                    api().isNormalButtonPressed() -> {
                        ActionsViewModel.runActionForConnection(context)
                    }
                }
            },
            EventAction("Disconnect") {
                isVisible = false
            }
        )

        // Substitute the original ProgressEvents.runEventActions logic here
        ProgressEvents.runEventActions("ActionRunner", eventActions)
    }

    // Show or hide based on the isVisible state
    if (isVisible) {
        Box(
            modifier = Modifier.fillMaxSize() // Example layout, you can customize this
        ) {
            AppText("Action Runner Layout") // Replace with actual UI content
        }
    }
}