package org.avmedia.gShockSmartSyncCompose

import android.Manifest
import android.app.Activity
import android.content.Context
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
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar
import org.avmedia.gShockSmartSyncCompose.ui.common.PopupMessageReceiver
import org.avmedia.gshockapi.GShockAPIMock

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

        // Check permissions on startup
    }

    @Composable
    fun DynamicContent() {
        RunWithChecks()
    }

    @Composable
    private fun RunWithChecks() {
        CheckPermissions()

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
