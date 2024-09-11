package org.avmedia.gShockSmartSyncCompose.utils

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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
        fun Snackbar(message: String, duration: Int, onDismiss: () -> Unit) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    LaunchedEffect(Unit) {
                        delay(duration.toLong())
                        onDismiss()
                    }
                },
                content = { Text(text = message) }
            )
        }
    }
}