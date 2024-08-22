package org.avmedia.gShockSmartSyncCompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    @Composable
    fun bottomNavigationItems(): List<BottomNavigationItem> {

        return listOf(
            BottomNavigationItem(
                label = "Time",
                icon = Icons.Filled.AccessTime,
                route = Screens.Time.route
            ),
            BottomNavigationItem(
                label = "Alarms",
                icon = Icons.Filled.Alarm,
                route = Screens.Alarms.route
            ),
            BottomNavigationItem(
                label = "Events",
                icon = Icons.Filled.CalendarToday,
                route = Screens.Events.route
            ),
            BottomNavigationItem(
                label = "Actions",
                icon =  Icons.AutoMirrored.Filled.DirectionsRun,
                route = Screens.Actions.route
            ),
            BottomNavigationItem(
                label = "Setting",
                icon = Icons.Filled.Settings,
                route = Screens.Settings.route
            ),
        )
    }
}