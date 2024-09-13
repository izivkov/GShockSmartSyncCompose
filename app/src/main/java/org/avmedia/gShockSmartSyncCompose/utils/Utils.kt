package org.avmedia.gShockSmartSyncCompose.utils

class Utils {
    companion object {
        fun AppHashCode(): String {
            val callingFunctionName = Thread.currentThread().stackTrace[3].methodName
            return callingFunctionName.hashCode().toString()
        }
    }
}