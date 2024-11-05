package org.avmedia.gShockSmartSyncCompose.ui.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.EventAction
import org.avmedia.gshockapi.ProgressEvents
import kotlin.coroutines.CoroutineContext

object AutoLightSetter {
    fun start() {

        val autoLightSetActions = arrayOf(
            EventAction("onSunrise") {
                CoroutineScope(Dispatchers.IO).launch {
                    val settings = api().getSettings()
                    settings.autoLight = false
                    api().setSettings(settings)
                }
            },
            EventAction("onSunset") {
                CoroutineScope(Dispatchers.IO).launch {
                    val settings = api().getSettings()
                    settings.autoLight = true
                    api().setSettings(settings)
                }
            }
        )

        ProgressEvents.runEventActions(Utils.AppHashCode(), autoLightSetActions)
    }
}