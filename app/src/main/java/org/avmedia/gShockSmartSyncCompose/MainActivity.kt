package org.avmedia.gShockSmartSyncCompose

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.services.DeviceManager
import org.avmedia.gShockSmartSyncCompose.services.InactivityWatcher
import org.avmedia.gShockSmartSyncCompose.services.NightWatcher
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.PopupMessageReceiver
import org.avmedia.gShockSmartSyncCompose.ui.common.SnackbarController
import org.avmedia.gShockSmartSyncCompose.ui.others.PreConnectionScreen
import org.avmedia.gShockSmartSyncCompose.ui.others.RunActionsScreen
import org.avmedia.gShockSmartSyncCompose.utils.CheckPermissions
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.GShockAPI
import org.avmedia.gshockapi.ProgressEvents
import timber.log.Timber

class MainActivity : ComponentActivity() {
    // Use FragmentActivity to be able to handle popups like MaterialTimePickerDialog in AlarmsItem
    private val api = GShockAPI(this)
    private var deviceManager: DeviceManager
    private var snackbarHostState: SnackbarHostState? = null

    init {
        instance = this

        // do not delete this. DeviceManager needs to be running to save the last device name to reuse on next start.
        deviceManager = DeviceManager
    }

    @Composable
    private fun Init() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            CheckPermissions {
                if (!api().isBluetoothEnabled(this)) {
                    turnOnBLE()
                }

                createAppEventsSubscription()

                InactivityWatcher.start(this)
                NightWatcher.setupSunriseSunsetTasks(this@MainActivity as Context)

                GShockSmartSyncTheme {
                    SnackbarController.snackbarHostState = remember { SnackbarHostState() }

                    PopupMessageReceiver()

                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = SnackbarController.snackbarHostState!!) },
                        modifier = Modifier.fillMaxSize()
                    ) { _ ->
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Init()
                            RunWithChecks()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun run() {
        AppScreen { PreConnectionScreen() }

        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            waitForConnectionCached()
        }
    }

    @Composable
    private fun RunWithChecks() {
        run()
    }

    @Composable
    fun AppScreen(content: @Composable () -> Unit) {
        GShockSmartSyncTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),  // Use full screen size for consistency
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }

    private fun createAppEventsSubscription() {

        val eventActions = arrayOf(
            EventAction("WatchInitializationCompleted") {

                setContent {
                    AppScreen {
                        if (api().isActionButtonPressed()) {
                            RunActionsScreen()
                        } else {
                            BottomNavigationBarWithPermissions()
                            PopupMessageReceiver()
                        }
                    }
                }
            },
            EventAction("ConnectionFailed") {
                setContent {
                    AppScreen {
                        PreConnectionScreen()
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
                    AppScreen {
                        PreConnectionScreen()
                    }
                }
            },
            EventAction("WaitForConnection")
            {
                Timber.i("WaitForConnection message received...")

                setContent {
                    RunWithChecks()
                }
            },
            EventAction("Disconnect")
            {
                Timber.i("onDisconnect")

                InactivityWatcher.cancel()
                val device = ProgressEvents.getPayload("Disconnect") as? BluetoothDevice
                if (device != null) {
                    api().teardownConnection(device)
                }
                setContent {
                    RunWithChecks()
                }
            },
            EventAction("HomeTimeUpdated")
            {},
        )

        ProgressEvents.runEventActions(Utils.AppHashCode(), eventActions)
    }

    private suspend fun waitForConnectionCached() {
        // Use this variable to control whether we should try to scan each time for the watch,
        // or reuse the last saved address. If set tto false, the connection is a bit slower,
        // but the app can connect to multiple watches without pressing "FORGET".
        // Also, auto-time-sync will work for multiple watches

        // Note: Consequently, we discovered that the Bluetooth scanning cannot be performed in the background,
        // so actions will fail. If this flag is true, no scanning will be performed.
        // Leave it to true.
        val reuseAddress = true
        var deviceAddress: String? = null

        if (reuseAddress) {
            val savedDeviceAddress =
                LocalDataStorage.get(applicationContext(), "LastDeviceAddress", "")
            if (api().validateBluetoothAddress(savedDeviceAddress)) {
                deviceAddress = savedDeviceAddress
            }
        }

        val deviceName = LocalDataStorage.get(applicationContext(), "LastDeviceName", "")
        api().waitForConnection(deviceAddress, deviceName)
    }

    private var requestBluetooth: ActivityResultLauncher<Intent> =
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
            finish()
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

        fun api(): GShockAPI {
            return instance!!.api
        }
    }
}
