package org.avmedia.gShockSmartSyncCompose.ui.settings

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gshockapi.WatchInfo
import org.json.JSONObject
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

object SettingsModel {
    abstract class Setting(open var title: String)

    private val _settings = MutableStateFlow<ArrayList<Setting>>(arrayListOf())
    val settings: StateFlow<ArrayList<Setting>> = _settings
    private val settingsList = _settings.value

    val settingsMap by lazy {
        settingsList.associateBy { it.title }.toMutableMap()
    }

    val locale by lazy { settingsMap["Locale"] }
    val buttonSound by lazy { settingsMap["Button Sound"] }
    val powerSavingMode by lazy { settingsMap["Power Saving Mode"] }
    val timeAdjustment by lazy { settingsMap["Time Adjustment"] }
    val dnd by lazy { settingsMap["DnD"] }
    val light by lazy { settingsMap["Light"] }
    val keep_alive by lazy { settingsMap["Keep Alive"] }
    val autoLightNightOnly by lazy { settingsMap["Auto Light Night Only"] }

    fun getLocale(): Locale {
        return settingsMap["Locale"] as Locale
    }

    fun getButtonSound(): OperationSound {
        return settingsMap["Button Sound"] as OperationSound
    }

    fun getPowerSavingMode(): PowerSavingMode {
        return settingsMap["Power Saving Mode"] as PowerSavingMode
    }

    fun getTimeAdjustment(): TimeAdjustment {
        return settingsMap["Time Adjustment"] as TimeAdjustment
    }

    fun getLight(): Light {
        return settingsMap["Light"] as Light
    }

    fun getDnD(): DnD {
        return settingsMap["DnD"] as DnD
    }
    // fun getKeepAlive(): KeepAlive {return settingsMap["Keep Alive"] as KeepAlive}
    // fun getAutoLightNightOnly(): AutoLightNightOnly {return settingsMap["Auto Light Night Only"] as AutoLightNightOnly}

    fun updateSetting(setting: Setting) {
        settingsMap[setting.title] = setting
    }

    class Locale : Setting("Locale") {
        enum class TIME_FORMAT(val value: String) {
            TWELVE_HOURS("12h"), TWENTY_FOUR_HOURS("24h"),
        }

        enum class DATE_FORMAT(val value: String) {
            MONTH_DAY("MM:DD"), DAY_MONTH("DD:MM"),
        }

        enum class DAY_OF_WEEK_LANGUAGE(var value: String) {
            ENGLISH("English"), SPANISH("Spanish"), FRENCH("French"), GERMAN("German"), ITALIAN("Italian"), RUSSIAN(
                "Russian"
            )
        }

        var timeFormat: TIME_FORMAT = TIME_FORMAT.TWELVE_HOURS
        var dateFormat: DATE_FORMAT = DATE_FORMAT.MONTH_DAY
        var dayOfWeekLanguage: DAY_OF_WEEK_LANGUAGE = DAY_OF_WEEK_LANGUAGE.ENGLISH
    }

    class OperationSound : Setting("Button Sound") {
        var sound: Boolean = true
    }

    class Light : Setting("Light") {
        enum class LIGHT_DURATION(val value: String) {
            TWO_SECONDS("2s"), FOUR_SECONDS("4s")
        }

        var autoLight: Boolean = false
        var duration: LIGHT_DURATION = LIGHT_DURATION.TWO_SECONDS
        var nightOnly: Boolean = LocalDataStorage.getAutoLightNightOnly(applicationContext())
    }

    class PowerSavingMode : Setting("Power Saving Mode") {
        var powerSavingMode: Boolean = false
    }

    class TimeAdjustment : Setting("Time Adjustment") {
        var timeAdjustment: Boolean = true
        var adjustmentTimeMinutes: Int = 0
        var timeAdjustmentNotifications: Boolean =
            LocalDataStorage.getTimeAdjustmentNotification(applicationContext())
    }

    class DnD : Setting("DnD") {
        var dnd: Boolean = true
        var mirrorPhone: Boolean = LocalDataStorage.getMirrorPhoneDnd(applicationContext())
    }

    class HandAdjustment : Setting("Hand Adjustment")

    init {
        settingsList.clear()
        settingsList.add(Locale())
        settingsList.add(OperationSound())
        settingsList.add(Light())
        settingsList.add(PowerSavingMode())
        settingsList.add(TimeAdjustment())
        settingsList.add(DnD())

        val coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()
        CoroutineScope(coroutineContext).launch {
            val settingStr = Gson().toJson(api().getSettings())
            fromJson(settingStr)
        }

        repeatEverySecond()
    }

    private fun repeatEverySecond() {

        val coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()
        CoroutineScope(coroutineContext).launch {
            val setting = api().getSettings()

            while (isActive) { // Ensure the coroutine can be cancelled
                val settingStr = Gson().toJson(setting)
                fromJson(settingStr)
                delay(1000)

                setting.autoLight = !setting.autoLight
            }
        }
    }

    @Synchronized
    fun fromJson(jsonStr: String) {
        /*
        jsonStr:
        {"adjustmentTimeMinutes":23, "autoLight":true,"dateFormat":"MM:DD",
        "language":"Spanish","lightDuration":"4s","powerSavingMode":true,
        "timeAdjustment":true, "timeFormat":"12h","timeTone":false, "DnD": "false"}
         */

        val jsonObj = JSONObject(jsonStr)
        val keys = jsonObj.keys()
        while (keys.hasNext()) {
            val key: String = keys.next()
            Timber.i("Key: $key")
            val value = jsonObj.get(key)
            when (key) {
                "powerSavingMode" -> {
                    if (WatchInfo.hasPowerSavingMode) {
                        val setting: PowerSavingMode = getPowerSavingMode()
                        setting.powerSavingMode = value == true
                    }
                }

                "timeAdjustment" -> {
                    if (!WatchInfo.alwaysConnected) { // Auto-time-adjustment does not apply for always-connected watches
                        val setting: TimeAdjustment = getTimeAdjustment()
                        setting.timeAdjustment = value == true
                    }
                }

                "adjustmentTimeMinutes" -> {
                    if (!WatchInfo.alwaysConnected) { // Auto-time-adjustment does not apply for always-connected watches
                        val setting: TimeAdjustment = getTimeAdjustment()
                        setting.adjustmentTimeMinutes = value as Int
                    }
                }

                "DnD" -> {
                    if (WatchInfo.hasDnD) {
                        val setting: DnD = getDnD()
                        setting.dnd = value == true
                    }
                }

                "buttonTone" -> {
                    val setting: OperationSound = getButtonSound()
                    setting.sound = value == true
                }

                "autoLight" -> {
                    val setting: Light = getLight()
                    setting.autoLight = value == true
                }

                "lightDuration" -> {
                    val setting: Light = getLight()
                    if (value == Light.LIGHT_DURATION.TWO_SECONDS.value) {
                        setting.duration = Light.LIGHT_DURATION.TWO_SECONDS
                    } else {
                        setting.duration = Light.LIGHT_DURATION.FOUR_SECONDS
                    }
                }

                "timeFormat" -> {
                    val setting: Locale = getLocale()
                    if (value == Locale.TIME_FORMAT.TWELVE_HOURS.value) {
                        setting.timeFormat = Locale.TIME_FORMAT.TWELVE_HOURS
                    } else {
                        setting.timeFormat = Locale.TIME_FORMAT.TWENTY_FOUR_HOURS
                    }
                }

                "dateFormat" -> {
                    val setting: Locale = getLocale()
                    if (value == Locale.DATE_FORMAT.MONTH_DAY.value) {
                        setting.dateFormat = Locale.DATE_FORMAT.MONTH_DAY
                    } else {
                        setting.dateFormat = Locale.DATE_FORMAT.DAY_MONTH
                    }
                }

                "language" -> {
                    val setting: Locale = getLocale()
                    when (value) {
                        Locale.DAY_OF_WEEK_LANGUAGE.ENGLISH.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.ENGLISH

                        Locale.DAY_OF_WEEK_LANGUAGE.SPANISH.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.SPANISH

                        Locale.DAY_OF_WEEK_LANGUAGE.FRENCH.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.FRENCH

                        Locale.DAY_OF_WEEK_LANGUAGE.GERMAN.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.GERMAN

                        Locale.DAY_OF_WEEK_LANGUAGE.ITALIAN.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.ITALIAN

                        Locale.DAY_OF_WEEK_LANGUAGE.RUSSIAN.value -> setting.dayOfWeekLanguage =
                            Locale.DAY_OF_WEEK_LANGUAGE.RUSSIAN
                    }
                }
            }
        }

        println("Settings updated: ${_settings.value}")
        _settings.value.forEach { setting ->
            if (setting.title == "Light") {
                println("------------> " + setting.title + ": " + (setting as Light).autoLight)
            }
        }

        settingsMap.clear()
        settingsList.forEach { setting ->
            settingsMap[setting.title] = setting
        }
    }

    fun getSettings(): ArrayList<Setting> {
        return filter(settingsList)
    }

    private fun filter(settings: ArrayList<Setting>): ArrayList<Setting> {
        return settings.filter { setting ->
            when (setting) {
                is PowerSavingMode -> WatchInfo.hasPowerSavingMode
                is TimeAdjustment -> !WatchInfo.alwaysConnected
                is DnD -> WatchInfo.hasDnD
                else -> true
            }
        } as ArrayList<Setting>
    }
}
