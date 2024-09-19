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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.PopupMessageReceiver
import org.avmedia.gshockapi.GShockAPIMock
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {
    // Use FragmentActivity to be able to handle popups like MaterialTimePickerDialog in AlarmsItem
    private val api = GShockAPIMock(this)

    init {
        instance = this
    }

    @Composable
    fun init() {
        BottomNavigationBarWithPermissions()
        PopupMessageReceiver()
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
                    init()
                    DynamicContent()
                }
            }
        }
    }

    @Composable
    private fun run() {
    }

    @Composable
    fun DynamicContent() {
        RunWithChecks()
    }

    @Composable
    private fun RunWithChecks() {

        CheckPermissions()

        // Check if Bluetooth is enabled
        if (!api().isBluetoothEnabled(this)) {
            turnOnBLE()
        }

        // Do more checks here
        run()
    }

    @Composable
    fun CheckPermissions() {
        val context = LocalContext.current

        val initialPermissions = emptyList<String>().toMutableList()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            initialPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            initialPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            initialPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        // Launchers for requesting permissions
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (!permissions.values.all { it }) {
                    setContent {
                        // Show a snackbar and exit the app
                        AppSnackbar("Permissions not granted. Exiting...")
                        (context as Activity).finish()
                    }
                }
            }
        )

        LaunchedEffect(Unit)
        {
            launcher.launch(initialPermissions.toTypedArray())
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
