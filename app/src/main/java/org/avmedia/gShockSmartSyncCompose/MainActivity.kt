package org.avmedia.gShockSmartSyncCompose

import AppText
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.utils.PermissionManager
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gShockSmartSyncCompose.utils.Utils.Companion.Snackbar
import org.avmedia.gshockapi.GShockAPIMock
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {
    private val api = GShockAPIMock(this)
    private lateinit var permissionManager: PermissionManager

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            runWithChecks()
        }
    }

    @Composable
    private fun run() {

        val navController = rememberNavController()
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
                if (permissions.values.all { it }) {
                    // If all permissions granted, navigate to Time screen
                    // navController.navigate(Screens.Time.route)
                } else {
                    setContent {
                        // Show a snackbar and exit the app
                        Snackbar("Permissions not granted. Exiting...", 3000) {
                            (context as Activity).finish()
                        }
                    }
                }
            }
        )

        // Check permissions on startup
        LaunchedEffect(Unit) {
            launcher.launch(initialPermissions.toTypedArray())
        }

        GShockSmartSyncTheme {
            Surface(
                modifier = Modifier.wrapContentHeight(),
                color = MaterialTheme.colorScheme.background,
            ) {
                DynamicContent()
            }
        }
    }

    @Composable
    fun DynamicContent() {
        BottomNavigationBar()
        // SecondScreen()
    }

    @Composable
    fun SecondScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AppText("This is the second screen!")
        }
    }

    @Composable
    private fun runWithChecks() {
        // Add Check here.
        run()
    }

    override fun onResume() {
        super.onResume()
        // runWithChecks()
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
