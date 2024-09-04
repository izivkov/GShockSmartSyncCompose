/*
 * Created by Ivo Zivkov (izivkov@gmail.com) on 2022-03-30, 12:06 a.m.
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 2022-03-20, 7:47 p.m.
 */

package org.avmedia.gShockSmartSyncCompose.ui.actions

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.SystemClock
import android.view.KeyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.services.NotificationProvider
import org.avmedia.gShockSmartSyncCompose.ui.actions.ActionsModel.CoroutineScopes.mainScope
import org.avmedia.gShockSmartSyncCompose.ui.events.EventsModel
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.WatchInfo
import timber.log.Timber
import java.text.DateFormat
import java.time.Clock
import java.util.Date

@Suppress(
    "ClassName",
)
object ActionsModel {

    private val actions = ArrayList<Action>()
    private val actionMap: MutableMap<String, Action> = mutableMapOf()

    enum class RUN_MODE {
        SYNC, ASYNC,
    }

    init {
        val findPhoneText = applicationContext().getString(R.string.find_phone)
        val findPhoneAction = FindPhoneAction(findPhoneText, true)
        actions.add(findPhoneAction)
        actionMap[findPhoneText] = findPhoneAction

        val setTimeText = applicationContext().getString(R.string.set_time)
        val timeAction = SetTimeAction(setTimeText, true)
        actions.add(timeAction)
        actionMap[setTimeText] = timeAction

        val setReminderText = applicationContext().getString(R.string.set_reminders)
        val setReminderAction = SetEventsAction(setReminderText, false)
        actions.add(setReminderAction)
        actionMap[setReminderText] = setReminderAction

        val takePhotoText = applicationContext().getString(R.string.take_photo)
        val takePhotoAction = PhotoAction(takePhotoText, false, CAMERA_ORIENTATION.BACK)
        actions.add(takePhotoAction)
        actionMap[takePhotoText] = takePhotoAction

        val toggleFlashlightText = applicationContext().getString(R.string.toggle_flashlight)
        val toggleFlashlightAction = ToggleFlashlightAction(toggleFlashlightText, false)
        actions.add(toggleFlashlightAction)
        actionMap[toggleFlashlightText] = toggleFlashlightAction

        val voiceAssistantText = applicationContext().getString(R.string.start_voice_assistant)
        val voiceAssistantAction = StartVoiceAssistAction(voiceAssistantText, false)
        actions.add(voiceAssistantAction)
        actionMap[voiceAssistantText] = voiceAssistantAction

        val nextTrackText = "Skip to next track"
        val nextTrackAction = NextTrack(nextTrackText, false)
        actions.add(nextTrackAction)
        actionMap[nextTrackText] = nextTrackAction

        val prayerAlarmsText = "Set Prayer Alarms"
        val prayerAlarmsAction = PrayerAlarmsAction(prayerAlarmsText, false)
        actions.add(prayerAlarmsAction)
        actionMap[prayerAlarmsText] = prayerAlarmsAction

        val emergencyActionsText = applicationContext().getString(R.string.emergency_actions)
        val emergencyActions = Separator(emergencyActionsText, false)
        actions.add(emergencyActions)
        actionMap[emergencyActionsText] = emergencyActions

        val makePhoneCallText = applicationContext().getString(R.string.make_phonecall)
        val makePhoneCallAction = PhoneDialAction(makePhoneCallText, false, "")
        actions.add(makePhoneCallAction)
        actionMap[makePhoneCallText] = makePhoneCallAction

        loadData(applicationContext())
    }

    fun getFlashlightAction(): ToggleFlashlightAction {
        return actionMap[applicationContext().getString(R.string.toggle_flashlight)] as ToggleFlashlightAction
    }

    fun getSetTimeAction(): SetTimeAction {
        return actionMap[applicationContext().getString(R.string.set_time)] as SetTimeAction
    }

    fun getSetReminderAction(): SetEventsAction {
        return actionMap[applicationContext().getString(R.string.set_reminders)] as SetEventsAction
    }

    fun getTakePhotoAction(): PhotoAction {
        return actionMap[applicationContext().getString(R.string.take_photo)] as PhotoAction
    }

    fun getVoiceAssistantAction(): StartVoiceAssistAction {
        return actionMap[applicationContext().getString(R.string.start_voice_assistant)] as StartVoiceAssistAction
    }

    fun getNextTrackAction(): NextTrack {
        return actionMap["Skip to next track"] as NextTrack
    }

    fun getPrayerAlarmsAction(): PrayerAlarmsAction {
        return actionMap["Set Prayer Alarms"] as PrayerAlarmsAction
    }

    fun getEmergencyActions(): Separator {
        return actionMap[applicationContext().getString(R.string.emergency_actions)] as Separator
    }

    fun getMakePhoneCallAction(): PhoneDialAction {
        return actionMap[applicationContext().getString(R.string.make_phonecall)] as PhoneDialAction
    }

    fun getActions(): ArrayList<Action> {
        return filter(actions)
    }

    private fun filter(actions: ArrayList<Action>): ArrayList<Action> {
        return actions.filter { action ->
            when (action) {
                is FindPhoneAction -> WatchInfo.findButtonUserDefined
                is SetEventsAction -> WatchInfo.hasReminders
                else -> true
            }
        } as ArrayList<Action>
    }

    enum class RunEnvironment {
        NORMAL_CONNECTION,      // Connected by long-pressing the LOWER-LEFT button
        ACTION_BUTTON_PRESSED,  // Connected by short-pressing the LOWER-RIGHT button
        AUTO_TIME_ADJUSTMENT,   // Connected automatically during auto time update
        FIND_PHONE_PRESSED      // The user has activated the "Find Phone" function
    }

    abstract class Action(
        open var title: String,
        open var enabled: Boolean,
        var runMode: RUN_MODE = RUN_MODE.SYNC,
    ) {
        open fun shouldRun(runEnvironment: RunEnvironment): Boolean {
            return when (runEnvironment) {
                RunEnvironment.ACTION_BUTTON_PRESSED -> enabled
                RunEnvironment.NORMAL_CONNECTION -> false
                RunEnvironment.AUTO_TIME_ADJUSTMENT -> false
                RunEnvironment.FIND_PHONE_PRESSED -> false
            }
        }

        abstract fun run(context: Context)

        open fun save(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            val value = enabled
            LocalDataStorage.put(context, key, value.toString())
        }

        open fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled = LocalDataStorage.get(context, key, "false").toBoolean()
            Timber.d("Load value: $key, $enabled")
        }

        open fun validate(context: Context): Boolean {
            return true
        }
    }

    class SetEventsAction(
        override var title: String, override var enabled: Boolean
    ) :
        Action(title, enabled, RUN_MODE.ASYNC) {

        override fun shouldRun(runEnvironment: RunEnvironment): Boolean {
            return when (runEnvironment) {
                RunEnvironment.NORMAL_CONNECTION -> enabled && WatchInfo.hasReminders
                RunEnvironment.ACTION_BUTTON_PRESSED -> enabled && WatchInfo.hasReminders
                RunEnvironment.AUTO_TIME_ADJUSTMENT -> enabled && WatchInfo.hasReminders
                RunEnvironment.FIND_PHONE_PRESSED -> false
            }
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            EventsModel.refresh(context)
            api().setEvents(EventsModel.events)
            // Utils.snackBar(context, "Events Sent to Watch")
        }

        override fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled = LocalDataStorage.get(context, key, "false").toBoolean()
        }
    }

    class ToggleFlashlightAction(override var title: String, override var enabled: Boolean) :
        Action(title, enabled) {

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            FlashlightHelper.toggle(context)
        }

        override fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled = LocalDataStorage.get(context, key, "false").toBoolean()
        }
    }

    class FindPhoneAction(override var title: String, override var enabled: Boolean) :
        Action(title, enabled) {

        override fun shouldRun(runEnvironment: RunEnvironment): Boolean {
            return when (runEnvironment) {
                RunEnvironment.NORMAL_CONNECTION -> false
                RunEnvironment.ACTION_BUTTON_PRESSED -> enabled && WatchInfo.findButtonUserDefined
                RunEnvironment.AUTO_TIME_ADJUSTMENT -> false
                RunEnvironment.FIND_PHONE_PRESSED -> true
            }
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            PhoneFinder.ring(context)
        }

        override fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled =
                LocalDataStorage.get(
                    context,
                    key,
                    if (WatchInfo.findButtonUserDefined) "true" else "false"
                )
                    .toBoolean()
        }
    }

    class SetTimeAction(
        override var title: String, override var enabled: Boolean
    ) :
        Action(
            title,
            enabled,
            RUN_MODE.ASYNC,
        ) {

        override fun shouldRun(runEnvironment: RunEnvironment): Boolean {
            return when (runEnvironment) {
                RunEnvironment.NORMAL_CONNECTION -> WatchInfo.alwaysConnected
                RunEnvironment.ACTION_BUTTON_PRESSED -> enabled
                RunEnvironment.AUTO_TIME_ADJUSTMENT -> true
                RunEnvironment.FIND_PHONE_PRESSED -> false
            }
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")

            // actions are sun on the main lifecycle scope, because the Actions Fragment never gets created.
            mainScope.launch {
                api().setTime()
            }
        }

        override fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled =
                LocalDataStorage.get(
                    context,
                    key,
                    if (WatchInfo.findButtonUserDefined) "false" else "true"
                )
                    .toBoolean()
        }
    }

    class SetLocationAction(override var title: String, override var enabled: Boolean) :
        Action(title, enabled) {
        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
        }
    }

    class StartVoiceAssistAction(override var title: String, override var enabled: Boolean) :
        Action(title, enabled, RUN_MODE.ASYNC) {
        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            try {
                context.startActivity(Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            } catch (e: ActivityNotFoundException) {
                Utils.snackBar(context, "Voice Assistant not available on this device!")
            }
        }
    }

    class NextTrack(override var title: String, override var enabled: Boolean) :
        Action(title, enabled, RUN_MODE.ASYNC) {
        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            try {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val eventTime = SystemClock.uptimeMillis()

                val downEvent = KeyEvent(
                    eventTime,
                    eventTime,
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                    0
                )
                audioManager.dispatchMediaKeyEvent(downEvent)

                val upEvent = KeyEvent(
                    eventTime,
                    eventTime,
                    KeyEvent.ACTION_UP,
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                    0
                )
                audioManager.dispatchMediaKeyEvent(upEvent)

            } catch (e: ActivityNotFoundException) {
                Utils.snackBar(context, "Cannot go to Next Track!")
            }
        }
    }

    class PrayerAlarmsAction(
        override var title: String, override var enabled: Boolean
    ) :
        Action(title, enabled, RUN_MODE.ASYNC) {

        override fun shouldRun(runEnvironment: RunEnvironment): Boolean {
            return when (runEnvironment) {
                RunEnvironment.NORMAL_CONNECTION -> enabled
                RunEnvironment.ACTION_BUTTON_PRESSED -> enabled
                RunEnvironment.AUTO_TIME_ADJUSTMENT -> enabled
                RunEnvironment.FIND_PHONE_PRESSED -> false
            }
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
            val alarms = PrayerAlarmsHelper.createPrayerAlarms(context)
            if (alarms == null) {
                Utils.snackBar(context, "Could not set prayer alarms")
                return
            }
            mainScope.launch {
                // getAlarms need to be run first, otherwise setAlarms() will not work
                api().getAlarms()
                api().setAlarms(alarms)
                // Utils.snackBar(context, "Set Prayer Alarms")
            }
        }
    }

    class Separator(override var title: String, override var enabled: Boolean) :
        Action(title, enabled) {
        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
        }

        override fun load(context: Context) {
            // Do nothing.
        }
    }

    class MapAction(override var title: String, override var enabled: Boolean) :
        Action(title, enabled) {
        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
        }
    }

    class PhoneDialAction(
        override var title: String, override var enabled: Boolean, var phoneNumber: String
    ) : Action(title, enabled) {
        init {
            Timber.d("PhoneDialAction")
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")

            val dialIntent =
                Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(dialIntent)
        }

        override fun save(context: Context) {
            super.save(context)
            val key = this.javaClass.simpleName + ".phoneNumber"
            LocalDataStorage.put(context, key, phoneNumber)
        }

        override fun load(context: Context) {
            super.load(context)
            val key = this.javaClass.simpleName + ".phoneNumber"
            phoneNumber = LocalDataStorage.get(context, key, "").toString()
        }

        override fun validate(context: Context): Boolean {
            if (phoneNumber.isEmpty()) {
                Utils.snackBar(context, "Phone number cannot be empty!")
                return false
            }

            return true
        }
    }

    enum class CAMERA_ORIENTATION {
        FRONT, BACK;
    }

    class PhotoAction(
        override var title: String,
        override var enabled: Boolean,
        var cameraOrientation: CAMERA_ORIENTATION
    ) : Action(title, enabled, RUN_MODE.ASYNC) {
        init {
            Timber.d("PhotoAction: orientation: $cameraOrientation")
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")

            var captureResult: String? = null
            val cameraHelper = CameraCaptureHelper(context)

            // Initialize the camera
            cameraHelper.initCamera()

            // Launch a coroutine to take the picture
            mainScope.launch {
                cameraHelper.takePicture(
                    onImageCaptured = { result ->
                        captureResult = result
                        Timber.d("Image captured: $captureResult")
                        // Handle result, maybe pass it to the UI or save it
                    },
                    onError = { error ->
                        captureResult = "Error: $error"
                        Timber.e("Camera capture error: $captureResult")
                    }
                )
            }
        }

        override fun save(context: Context) {
            super.save(context)
            val key = this.javaClass.simpleName + ".cameraOrientation"
            LocalDataStorage.put(context, key, cameraOrientation.toString())
        }

        override fun load(context: Context) {
            super.load(context)
            val key = this.javaClass.simpleName + ".cameraOrientation"
            cameraOrientation = if (LocalDataStorage.get(context, key, "BACK")
                    .toString() == "BACK"
            ) CAMERA_ORIENTATION.BACK else CAMERA_ORIENTATION.FRONT
        }
    }

    class EmailLocationAction(
        override var title: String,
        override var enabled: Boolean,
        var emailAddress: String,
        private var extraText: String
    ) : Action(title, enabled) {

        init {
            Timber.d("EmailLocationAction: emailAddress: $emailAddress")
            Timber.d("EmailLocationAction: extraText: $extraText")
        }

        override fun run(context: Context) {
            Timber.d("running ${this.javaClass.simpleName}")
        }

        override fun save(context: Context) {
            val key = this.javaClass.simpleName + ".emailAddress"
            LocalDataStorage.put(context, key, emailAddress)
            super.save(context)
        }

        override fun load(context: Context) {
            super.load(context)

            val key = this.javaClass.simpleName + ".emailAddress"
            emailAddress = LocalDataStorage.get(context, key, "").toString()
            extraText =
                "Sent by G-shock App:\n https://play.google.com/store/apps/details?id=org.avmedia.gshockGoogleSync"
        }
    }

    object FileSpecs {
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    /*
    Note: Alternatively, actions can run autonomously, when certain conditions were met:
    1. User pressed Action button (lower-right) on the watch
    2. The action is enabled
    3. Certain progress event received.

    However, this way gives us more control on how to start the actions.
     */
    private fun runIt(action: Action, context: Context) {
        try {
            action.run(context)
        } catch (e: SecurityException) {
            Utils.snackBar(
                context, "You have not given permission to to run action ${action.title}."
            )
        } catch (e: Exception) {
            Utils.snackBar(context, "Could not run action ${action.title}. Reason: $e")
        }
    }

    fun runActionsForActionButton(context: Context) {
        runFilteredActions(
            context,
            actions.filter { it.shouldRun(RunEnvironment.ACTION_BUTTON_PRESSED) })
    }

    fun runActionForConnection(context: Context) {
        runFilteredActions(context, actions.filter {
            it.shouldRun(RunEnvironment.NORMAL_CONNECTION)
        })
    }

    fun runActionsForAutoTimeSetting(context: Context) {
        runFilteredActions(
            context,
            actions.filter { it.shouldRun(RunEnvironment.AUTO_TIME_ADJUSTMENT) })

        // show notification if configured
        if (LocalDataStorage.getTimeAdjustmentNotification(context)
            && !WatchInfo.alwaysConnected
        ) { // only create notification for not-always connected watches.
            showTimeSyncNotification(context)
        }
    }

    fun runActionFindPhone(context: Context) {
        runFilteredActions(context, actions.filter {
            it.shouldRun(RunEnvironment.FIND_PHONE_PRESSED)
        })
    }

    private fun showTimeSyncNotification(context: Context) {
        val dateStr =
            DateFormat.getDateTimeInstance().format(Date(Clock.systemDefaultZone().millis()))

        var msg = "Time set at $dateStr"
        val watchName = WatchInfo.name
        msg += " for $watchName watch"

        NotificationProvider.createNotification(
            context,
            "G-Shock Smart Sync",
            msg,
            NotificationManager.IMPORTANCE_DEFAULT
        )
    }

    private fun runFilteredActions(context: Context, filteredActions: List<Action>) {

        filteredActions.sortedWith(compareBy { it.runMode.ordinal }) // run SYNC actions first
            .forEach {
                if (it.runMode == RUN_MODE.ASYNC) {
                    Timber.d("running ${it.javaClass.simpleName}")
                    // actions are run on the main lifecycle scope, because the Actions Fragment never gets created.
                    mainScope.launch {
                        runIt(it, context)
                    }
                } else {
                    runIt(it, context)
                }
            }
    }

    fun loadData(context: Context) {
        actions.forEach {
            it.load(context)
        }
    }

    fun saveData(context: Context) {
        actions.forEach {
            it.save(context)
        }
    }

    object CoroutineScopes {
        val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }
}