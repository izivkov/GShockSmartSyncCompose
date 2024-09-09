package org.avmedia.gShockSmartSyncCompose.ui.settings

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.api
import org.avmedia.gShockSmartSyncCompose.MainActivity.Companion.applicationContext
import org.avmedia.gShockSmartSyncCompose.utils.LocalDataStorage
import org.avmedia.gshockapi.WatchInfo
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

object SettingsViewModel : ViewModel() {
    abstract class Setting(open var title: String) {
        open fun save() {}
    }

    private val _settings = MutableStateFlow<ArrayList<Setting>>(arrayListOf())
    val settings: StateFlow<ArrayList<Setting>> = _settings
    private val settingsMap: MutableMap<Class<out Setting>, Setting> =
        _settings.value.associateBy { it::class.java }.toMutableMap()

    private fun updateSettingsAndMap(newSettings: ArrayList<Setting>) {
        settingsMap.clear()
        _settings.value = arrayListOf()

        newSettings.forEach { setting ->
            _settings.value.add(setting)
            settingsMap[setting::class.java] = setting
        }
    }

    fun <T : Setting> getSetting(type: Class<T>): T {
        return settingsMap[type] as T
    }

    fun <T : Setting> updateSetting(updatedSetting: T) {
        val currentList = _settings.value
        val index = currentList.indexOfFirst { it::class == updatedSetting::class }
        if (index != -1) {
            currentList[index] = updatedSetting
            updateSettingsAndMap(currentList)
            updatedSetting.save()
        }
    }

    data class Locale(
        var timeFormat: TIME_FORMAT = TIME_FORMAT.TWELVE_HOURS,
        var dateFormat: DATE_FORMAT = DATE_FORMAT.MONTH_DAY,
        var dayOfWeekLanguage: DAY_OF_WEEK_LANGUAGE = DAY_OF_WEEK_LANGUAGE.ENGLISH,
        var language: String = "English",
    ) : Setting("Locale") {
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
    }

    class OperationSound : Setting("Button Sound") {
        var sound: Boolean = true
    }

    data class Light(
        var autoLight: Boolean = false,
        var duration: LIGHT_DURATION = LIGHT_DURATION.TWO_SECONDS,
        var nightOnly: Boolean = LocalDataStorage.getAutoLightNightOnly(applicationContext())
    ) : Setting("Light") {
        enum class LIGHT_DURATION(val value: String) {
            TWO_SECONDS("2s"), FOUR_SECONDS("4s")
        }
    }

    data class PowerSavingMode(var powerSavingMode: Boolean = false) :
        Setting("Power Saving Mode")

    data class TimeAdjustment(
        var timeAdjustment: Boolean = true,
        var adjustmentTimeMinutes: Int = 0,
        var timeAdjustmentNotifications: Boolean =
            LocalDataStorage.getTimeAdjustmentNotification(applicationContext())
    ) : Setting("Time Adjustment") {
        override fun save() {
            LocalDataStorage.setTimeAdjustmentNotification(
                applicationContext(),
                timeAdjustmentNotifications
            )
        }
    }

    data class DnD(
        var dnd: Boolean = true,
        var mirrorPhone: Boolean = LocalDataStorage.getMirrorPhoneDnd(applicationContext())
    ) : Setting("DnD")

    init {
        val newSettings = arrayListOf(
            Locale(),
            OperationSound(),
            Light(),
            PowerSavingMode(),
            TimeAdjustment(),
            DnD()
        )
        updateSettingsAndMap(newSettings)

        val coroutineContext: CoroutineContext = Dispatchers.Default + SupervisorJob()
        CoroutineScope(coroutineContext).launch {
            val settingStr = Gson().toJson(api().getSettings())
            updateSettingsAndMap(fromJson(settingStr))
        }
    }

    @Synchronized
    fun fromJson(jsonStr: String): ArrayList<Setting> {
        /*
        jsonStr:
        {"adjustmentTimeMinutes":23, "autoLight":true,"dateFormat":"MM:DD",
        "language":"Spanish","lightDuration":"4s","powerSavingMode":true,
        "timeAdjustment":true, "timeFormat":"12h","timeTone":false, "DnD": "false"}
        */

        // Create a Set to store updated objects and avoid duplicates
        val updatedObjects = mutableSetOf<Setting>()

        val jsonObj = JSONObject(jsonStr)
        val keys = jsonObj.keys()

        while (keys.hasNext()) {
            val key: String = keys.next()
            val value = jsonObj.get(key)
            when (key) {
                "powerSavingMode" -> {
                    if (WatchInfo.hasPowerSavingMode) {
                        val setting: PowerSavingMode =
                            settingsMap[PowerSavingMode::class.java] as PowerSavingMode
                        setting.powerSavingMode = value == true
                        updatedObjects.add(setting)
                    }
                }

                "timeAdjustment" -> {
                    if (!WatchInfo.alwaysConnected) {
                        val setting: TimeAdjustment =
                            settingsMap[TimeAdjustment::class.java] as TimeAdjustment
                        setting.timeAdjustment = value == true
                        updatedObjects.add(setting)
                    }
                }

                "adjustmentTimeMinutes" -> {
                    if (!WatchInfo.alwaysConnected) {
                        val setting: TimeAdjustment =
                            settingsMap[TimeAdjustment::class.java] as TimeAdjustment
                        setting.adjustmentTimeMinutes = value as Int
                        updatedObjects.add(setting)
                    }
                }

                "DnD" -> {
                    if (WatchInfo.hasDnD) {
                        val setting: DnD = settingsMap[DnD::class.java] as DnD
                        setting.dnd = value == true
                        updatedObjects.add(setting)
                    }
                }

                "buttonTone" -> {
                    val setting: OperationSound =
                        settingsMap[OperationSound::class.java] as OperationSound
                    setting.sound = value == true
                    updatedObjects.add(setting)
                }

                "autoLight" -> {
                    val setting: Light = settingsMap[Light::class.java] as Light
                    setting.autoLight = value == true
                    updatedObjects.add(setting)
                }

                "lightDuration" -> {
                    val setting: Light = settingsMap[Light::class.java] as Light
                    if (value == Light.LIGHT_DURATION.TWO_SECONDS.value) {
                        setting.duration = Light.LIGHT_DURATION.TWO_SECONDS
                    } else {
                        setting.duration = Light.LIGHT_DURATION.FOUR_SECONDS
                    }
                    updatedObjects.add(setting)
                }

                "timeFormat" -> {
                    val setting: Locale = settingsMap[Locale::class.java] as Locale
                    if (value == Locale.TIME_FORMAT.TWELVE_HOURS.value) {
                        setting.timeFormat = Locale.TIME_FORMAT.TWELVE_HOURS
                    } else {
                        setting.timeFormat = Locale.TIME_FORMAT.TWENTY_FOUR_HOURS
                    }
                    updatedObjects.add(setting)
                }

                "dateFormat" -> {
                    val setting: Locale = settingsMap[Locale::class.java] as Locale
                    if (value == Locale.DATE_FORMAT.MONTH_DAY.value) {
                        setting.dateFormat = Locale.DATE_FORMAT.MONTH_DAY
                    } else {
                        setting.dateFormat = Locale.DATE_FORMAT.DAY_MONTH
                    }
                    updatedObjects.add(setting)
                }

                "language" -> {
                    val setting: Locale = settingsMap[Locale::class.java] as Locale
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
                    updatedObjects.add(setting)
                }
            }
        }

        // Return the updated objects as an ArrayList
        return ArrayList(updatedObjects)
    }


    fun getSettings(): ArrayList<Setting> {
        return filter(_settings.value)
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
