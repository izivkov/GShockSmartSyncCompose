package org.avmedia.gShockSmartSyncCompose

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.services.InactivityWatcher
import org.avmedia.gShockSmartSyncCompose.services.NightWatcher
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.PopupMessageReceiver
import org.avmedia.gShockSmartSyncCompose.ui.others.PreConnectionScreen
import org.avmedia.gShockSmartSyncCompose.ui.others.RunActionsScreen
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.GShockAPIMock
import org.avmedia.gshockapi.ProgressEvents
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {
    // Use FragmentActivity to be able to handle popups like MaterialTimePickerDialog in AlarmsItem
    private val api = GShockAPIMock(this)

    init {
        instance = this
    }

    @Composable
    private fun Init() {
        InactivityWatcher.start(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            GShockSmartSyncTheme {
                Surface(
                    modifier = Modifier.wrapContentHeight(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Init()
                    RunWithChecks()
                }
            }
        }
    }

    @Composable
    private fun run() {
        createAppEventsSubscription()
        PreConnectionScreen()

        LaunchedEffect(Unit) {
            val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
            scope.launch {
                api().waitForConnection(api().getWatchName())
            }
        }
    }

    @Composable
    private fun RunWithChecks() {

        CheckPermissions {
            NightWatcher.setupSunriseSunsetTasks(this@MainActivity as Context)

            // Check if Bluetooth is enabled
            if (!api().isBluetoothEnabled(this)) {
                turnOnBLE()
            }

            // Do more checks here
            run()
        }
    }

    private fun createAppEventsSubscription() {

        val eventActions = arrayOf(
            EventAction("ConnectionSetupComplete") {
                setContent {
                    GShockSmartSyncTheme {
                        if (api().isActionButtonPressed()) {
                            RunActionsScreen()
                        } else {
                            BottomNavigationBarWithPermissions()
                        }
                        PopupMessageReceiver()
                    }
                }
            },
            EventAction("ConnectionFailed") {
                setContent {
                    GShockSmartSyncTheme {
                        PreConnectionScreen()
                        PopupMessageReceiver()
                    }
                }
            },
            EventAction("ApiError")
            {
                val message = ProgressEvents.getPayload("ApiError") as String?
                    ?: "ApiError! Something went wrong - Make sure the official G-Shock app in not running, to prevent interference."

                AppSnackbar(message)
                api().disconnect(this)
                setContent {
                    GShockSmartSyncTheme {
                        PreConnectionScreen()
                    }
                }
            },
            EventAction("WaitForConnection")
            {},
            EventAction("Disconnect")
            {
                InactivityWatcher.cancel()
//                val device = ProgressEvents.getPayload("Disconnect") as BluetoothDevice
//                api().teardownConnection(device)
                setContent {
                    GShockSmartSyncTheme {
                        Surface(
                            modifier = Modifier.wrapContentHeight(),
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            PreConnectionScreen()
                        }
                    }
                }
            },
            EventAction("HomeTimeUpdated")
            {},
        )

        ProgressEvents.runEventActions(Utils.AppHashCode(), eventActions)
    }

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
            activity.finish()  // Exit if user doesn't want to open settings
        }
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                AppSnackbar("Bluetooth enabled.")
            } else {
                val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
                scope.launch {
                    AppSnackbar("Please enable Bluetooth in your settings and try again")
                    finish()
                }
            }
        }

    // @SuppressLint("MissingPermission")
    private fun turnOnBLE() {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            AppSnackbar("Sorry, your device does not support Bluetooth. Exiting...")
            Timer("SettingUp", false).schedule(6000) { finish() }
        }

        //val REQUEST_ENABLE_BT = 99
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            try {
                requestBluetooth.launch(enableBtIntent)
            } catch (e: SecurityException) {
                AppSnackbar(
                    "You have no permissions to turn on Bluetooth. Please turn it on manually."
                )
            }
        }
    }


    companion object {

        private var instance: MainActivity? = null

        // Make context available from anywhere in the code (not yet used).
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun api(): GShockAPIMock {
            return instance!!.api
        }
    }
}
