package org.avmedia.gShockSmartSyncCompose.ui.actions

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.SystemClock
import android.view.KeyEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.services.NotificationProvider
import org.avmedia.gShockSmartSyncCompose.ui.actions.ActionsViewModel.CoroutineScopes.mainScope
import org.avmedia.gShockSmartSyncCompose.ui.events.EventsModel
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gShockSmartSyncCompose.utils.Utils
import org.avmedia.gshockapi.WatchInfo
import timber.log.Timber
import java.text.DateFormat
import java.time.Clock
import java.util.Date

object ActionsViewModel : ViewModel() {
    private val _actions = MutableStateFlow<List<Action>>(emptyList())
    val actions: StateFlow<List<Action>> = _actions

    private val actionMap = mutableMapOf<Class<out Action>, Action>()

    enum class RUN_MODE {
        SYNC, ASYNC,
    }

    // Initialize or update the map when actions are loaded
    private fun updateActionTypeMap() {
        actionMap.clear()
        _actions.value.forEach { action ->
            actionMap[action::class.java] = action
        }
    }

    fun <T : Action> getAction(type: Class<T>): T {
        return actionMap[type] as T
    }

    init {
        loadInitialActions()
        loadData(applicationContext())
        updateActionTypeMap()
    }

    // Method to load the initial list of actions
    private fun loadInitialActions() {
        val initialActions = listOf(
            ToggleFlashlightAction("Toggle Flashlight", false),
            StartVoiceAssistAction("Start Voice Assistant", false),
            NextTrack("Skip to next track", false),

            FindPhoneAction(applicationContext().getString(R.string.find_phone), true),
            SetTimeAction(applicationContext().getString(R.string.set_time), true),
            SetEventsAction(applicationContext().getString(R.string.set_reminders), false),
            PhotoAction(
                applicationContext().getString(R.string.take_photo),
                false,
                CAMERA_ORIENTATION.BACK
            ),
            PrayerAlarmsAction("Set Prayer Alarms", true),
            Separator(applicationContext().getString(R.string.emergency_actions), false),
            PhoneDialAction(applicationContext().getString(R.string.make_phonecall), false, ""),
        )

        _actions.value = initialActions
    }

    // Function to update a specific action
    fun updateAction(updatedAction: Action) {
        _actions.value = _actions.value.map { action ->
            if (action.title == updatedAction.title) updatedAction else action
        }
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
        }

        override fun load(context: Context) {
            val key = this.javaClass.simpleName + ".enabled"
            enabled = LocalDataStorage.get(context, key, "false").toBoolean()
        }

        companion object {
            fun empty() = SetEventsAction("", false)
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

        companion object {
            fun empty() = ToggleFlashlightAction("", false)
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

        companion object {
            fun empty() = FindPhoneAction("", false)
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

        companion object {
            fun empty() = SetTimeAction("", false)
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

        companion object {
            fun empty() = StartVoiceAssistAction("", false)
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

        companion object {
            fun empty() = NextTrack("", false)
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
            }
        }

        companion object {
            fun empty() = PrayerAlarmsAction("", true)
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

        companion object {
            fun empty() = PhoneDialAction("", false, phoneNumber = "")
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

        companion object {
            fun empty() = PhotoAction("", false, cameraOrientation = CAMERA_ORIENTATION.BACK)
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
            _actions.value.filter { it.shouldRun(RunEnvironment.ACTION_BUTTON_PRESSED) })
    }

    fun runActionForConnection(context: Context) {
        runFilteredActions(context, _actions.value.filter {
            it.shouldRun(RunEnvironment.NORMAL_CONNECTION)
        })
    }

    fun runActionsForAutoTimeSetting(context: Context) {
        runFilteredActions(
            context,
            _actions.value.filter { it.shouldRun(RunEnvironment.AUTO_TIME_ADJUSTMENT) })

        // show notification if configured
        if (LocalDataStorage.getTimeAdjustmentNotification(context)
            && !WatchInfo.alwaysConnected
        ) { // only create notification for not-always connected watches.
            showTimeSyncNotification(context)
        }
    }

    fun runActionFindPhone(context: Context) {
        runFilteredActions(context, _actions.value.filter {
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
        _actions.value.forEach {
            it.load(context)
        }
    }

    fun saveData(context: Context) {
        _actions.value.forEach {
            it.save(context)
        }
    }

    object CoroutineScopes {
        val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }
}
