package org.avmedia.gShockSmartSyncCompose.utils

import android.content.Context
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
    }
}