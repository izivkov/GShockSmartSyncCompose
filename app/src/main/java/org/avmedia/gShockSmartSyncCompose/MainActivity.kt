package org.avmedia.gShockSmartSyncCompose

import AppText
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.theme.GShockSmartSyncTheme
import org.avmedia.gShockSmartSyncCompose.utils.PermissionManager
import org.avmedia.gshockapi.GShockAPIMock

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
            GShockSmartSyncTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.wrapContentHeight(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DynamicContent()
                }
            }
        }
        permissionManager = PermissionManager(this)
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

    private fun runWithChecks() {

        if (!permissionManager.hasAllPermissions()) {
            permissionManager.setupPermissions()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        runWithChecks()
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
