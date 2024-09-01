package org.avmedia.gShockSmartSyncCompose.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppSnackbar {
    private val snackbarHostState = SnackbarHostState()

    fun showSnackbar(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long)
        }
    }
}