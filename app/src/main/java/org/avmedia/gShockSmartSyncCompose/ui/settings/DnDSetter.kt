package org.avmedia.gShockSmartSyncCompose.ui.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents
import org.avmedia.gshockapi.WatchInfo

object DnDSetter {
    fun start() {
        val dnDSetActions = arrayOf(
            EventAction("DnD On") {
                if (WatchInfo.hasDnD && LocalDataStorage.getMirrorPhoneDnd(applicationContext())) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val settings = api().getSettings()
                        settings.DnD = true
                        api().setSettings(settings)
                    }
                }
            },
            EventAction("DnD Off") {
                if (WatchInfo.hasDnD && LocalDataStorage.getMirrorPhoneDnd(applicationContext())) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val settings = api().getSettings()
                        settings.DnD = false
                        api().setSettings(settings)
                    }
                }
            },
        )

        ProgressEvents.runEventActions(Utils.AppHashCode(), dnDSetActions)
    }
}