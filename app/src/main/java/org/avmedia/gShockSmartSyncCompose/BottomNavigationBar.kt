package org.avmedia.gShockSmartSyncCompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.avmedia.gShockSmartSyncCompose.ui.actions.ActionsScreen
import org.avmedia.gShockSmartSyncCompose.ui.alarms.AlarmsScreen
import org.avmedia.gShockSmartSyncCompose.ui.events.EventsScreen
import org.avmedia.gShockSmartSyncCompose.ui.settings.SettingsScreen
import org.avmedia.gShockSmartSyncCompose.ui.time.TimeScreen

@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination?.route,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Time.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screens.Time.route) {
                TimeScreen(
                    navController
                )
            }
            composable(Screens.Alarms.route) {
                AlarmsScreen(
                    navController
                )
            }
            composable(Screens.Events.route) {
                EventsScreen(
                    navController
                )
            }
            composable(Screens.Actions.route) {
                ActionsScreen(
                    navController
                )
            }
            composable(Screens.Settings.route) {
                SettingsScreen(
                    navController
                )
            }
        }
    }
}