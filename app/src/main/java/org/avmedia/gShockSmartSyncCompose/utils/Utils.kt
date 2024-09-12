package org.avmedia.gShockSmartSyncCompose.utils

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.ui.common.AppSnackbar

class Utils {
    companion object {
        fun AppHashCode(): String {
            val callingFunctionName = Thread.currentThread().stackTrace[3].methodName
            return callingFunctionName.hashCode().toString()
        }

        fun snackBar(context: Context, message: String) {
            AppSnackbar.showSnackbar(message)
        }

        @Composable
        fun Snackbar(
            message: String,
            duration: SnackbarDuration,
            onDismiss: () -> Unit
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            // Box to overlay Snackbar at the bottom
            Box(
                modifier = Modifier
                    .fillMaxSize() // Ensure Box fills the entire size of the screen
            ) {
                // SnackbarHost to manage displaying Snackbar
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            }

            // Trigger snackbar on some event
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = duration
                    ).also {
                        onDismiss()
                    }
                }
            }
        }
    }
}