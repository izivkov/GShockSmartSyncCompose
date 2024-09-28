package org.avmedia.gShockSmartSyncCompose.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.PopupMessageReceiver
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun CheckPermissions(onPermissionsGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity

    val initialPermissions = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    // State to track if permissions are granted
    var permissionsGranted by remember { mutableStateOf(false) }
    var showRationaleDialog by remember { mutableStateOf(false) }
    var permanentlyDenied by remember { mutableStateOf(false) }

    // Launcher for requesting permissions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsGranted = permissions.values.all { it }
            showRationaleDialog = permissions.values.any {
                !it && activity.shouldShowRequestPermissionRationale(initialPermissions[0])
            }
            permanentlyDenied = !permissionsGranted && !showRationaleDialog

            if (!permissionsGranted && !showRationaleDialog) {
                // If permissions are permanently denied (Don't ask again selected), set the flag
                permanentlyDenied = true
            }
        }
    )

    // Trigger the permission request on first composition
    LaunchedEffect(Unit) {
        launcher.launch(initialPermissions.toTypedArray())
    }

    // If permissions are granted, call the provided callback
    if (permissionsGranted) {
        onPermissionsGranted()
    }

    // Show rationale dialog if needed
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(text = "Permissions Required") },
            text = { Text("This app needs location and Bluetooth permissions to function properly.") },
            confirmButton = {
                TextButton(onClick = {
                    launcher.launch(initialPermissions.toTypedArray())
                }) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    activity.finish()  // Exit if user doesn't want to grant permissions
                }) {
                    Text("Exit")
                }
            }
        )
    }

    if (permanentlyDenied) {
        AppSnackbar("Permissions are permanently denied. Please enable them in the app settings.")
        Timer("SettingUp", false).schedule(6000) { activity.finish() }
    }
}

