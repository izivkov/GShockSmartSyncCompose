package org.avmedia.gshockapi

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.avmedia.gshockapi.ble.Connection
import org.avmedia.gshockapi.casio.*
import org.avmedia.gshockapi.io.*
import org.avmedia.gshockapi.utils.*
import timber.log.Timber
import java.time.DayOfWeek
import java.time.Month
import java.time.ZoneId
import java.util.*


/*
This class is used during development to mock the GShock API.
 */
@RequiresApi(Build.VERSION_CODES.O)
class GShockAPIMock(private val context: Context) {

    suspend fun waitForConnection(deviceId: String? = "", deviceName: String? = "") {
    }

    private suspend fun init(): Boolean {
        return true
    }

    fun isConnected(): Boolean {
        return true
    }

    fun teardownConnection(device: BluetoothDevice) {
        // NO-OP
    }

    suspend fun getPressedButton(): IO.WATCH_BUTTON {
        return IO.WATCH_BUTTON.LOWER_LEFT
    }

    fun isActionButtonPressed(): Boolean {
        return false
    }

    fun isNormalButtonPressed(): Boolean {
        return true
    }

    fun isAutoTimeStarted(): Boolean {
        return false
    }

    fun isFindPhoneButtonPressed(): Boolean {
        return false
    }

    suspend fun getWatchName(): String {
        return "GW-B5600"
    }

    suspend fun getError(): String {
        return "Error"
    }

    suspend fun getDSTWatchState(state: IO.DTS_STATE): String {
        /*
        DST STATE ZERO: 0x1D 00 01 03 02 02 76 00 00 FF FF FF FF FF FF
        DST STATE TWO: 0x1D 02 03 03 03 A0 00 DC 00 FF FF FF FF FF FF
        DST STATE FOUR: 0x1D 04 05 02 03 7A 00 CA 00 FF FF FF FF FF FF
         */
        return "0x1D 00 01 03 02 02 76 00 00 FF FF FF FF FF FF"
    }

    suspend fun getDSTForWorldCities(cityNumber: Int): String {
        return when (cityNumber) {
            0 -> "0x1E 00 02 76 EC 04 01"
            1 -> "0x1E 01 00 00 00 00 00"
            2 -> "0x1E 02 A0 00 00 04 02"
            3 -> "0x1E 03 DC 00 04 04 02"
            4 -> "0x1E 04 7A 00 20 04 00"
            5 -> "0x1E 05 CA 00 EC 04 01"
            else -> ""
        }
    }

    suspend fun getWorldCities(cityNumber: Int): String {
        return when (cityNumber) {
            0 -> "0x1F 00 54 4F 52 4F 4E 54 4F 00 00 00 00 00 00 00 00 00 00 00"
            1 -> "0x1F 01 28 55 54 43 29 00 00 00 00 00 00 00 00 00 00 00 00 00"
            2 -> "0x1F 02 4C 4F 4E 44 4F 4E 00 00 00 00 00 00 00 00 00 00 00 00"
            3 -> "0x1F 03 50 41 52 49 53 00 00 00 00 00 00 00 00 00 00 00 00 00"
            4 -> "0x1F 04 48 4F 4E 47 20 4B 4F 4E 47 00 00 00 00 00 00 00 00 00"
            5 -> "00x1F 05 4E 45 57 20 59 4F 52 4B 00 00 00 00 00 00 00 00 00 00"
            else -> ""
        }
    }

    suspend fun getHomeTime(): String {
        return "SOFIA"
    }

    suspend fun getBatteryLevel(): Int {
        return 73
    }

    suspend fun getWatchTemperature(): Int {
        return 36
    }

    suspend fun getTimer(): Int {
        return 4 * 60
    }

    fun setTimer(timerValue: Int) {
        // NO-OP
    }

    suspend fun getAppInfo(): String {
        return "0x22 C7 67 B2 F0 78 86 71 6A 76 EC 02"
    }

    suspend fun setTime(timeZone: String = TimeZone.getDefault().id) {
        // NO-OP
    }

    suspend fun getAlarms(): ArrayList<Alarm> {
        val alarmList: ArrayList<Alarm> = arrayListOf(
            Alarm(hour = 6, minute = 45, enabled = false, hasHourlyChime = true),
            Alarm(hour = 8, minute = 0, enabled = false, hasHourlyChime = false),
            Alarm(hour = 20, minute = 0, enabled = true, hasHourlyChime = false),
            Alarm(hour = 20, minute = 50, enabled = false, hasHourlyChime = false),
            Alarm(hour = 9, minute = 25, enabled = false, hasHourlyChime = false)
        )
        return alarmList
    }

    fun setAlarms(alarms: ArrayList<Alarm>) {
        // NO-OP
    }

    suspend fun getEventsFromWatch(): ArrayList<Event> {

        val eventList: ArrayList<Event> = arrayListOf(
            Event(
                title = "Event 1",
                startDate = EventDate(2024, Month.MAY, 1), // Replace with actual EventDate structure
                endDate = EventDate(2024,Month.MAY,2),
                repeatPeriod = RepeatPeriod.NEVER,
                daysOfWeek = arrayListOf(DayOfWeek.MONDAY),
                enabled = false,
                incompatible = false
            ),
            Event(
                title = "Event 2",
                startDate = EventDate(2024, Month.MAY, 1), // Replace with actual EventDate structure
                endDate = EventDate(2024,Month.MAY,2),
                repeatPeriod = RepeatPeriod.NEVER,
                daysOfWeek = arrayListOf(DayOfWeek.MONDAY),
                enabled = false,
                incompatible = false
            ),
            Event(
                title = "Event 3",
                startDate = EventDate(2024, Month.MAY, 1), // Replace with actual EventDate structure
                endDate = EventDate(2024,Month.MAY,2),
                repeatPeriod = RepeatPeriod.NEVER,
                daysOfWeek = arrayListOf(DayOfWeek.MONDAY),
                enabled = false,
                incompatible = false
            ),
            Event(
                title = "Event 4",
                startDate = EventDate(2024, Month.MAY, 1), // Replace with actual EventDate structure
                endDate = EventDate(2024,Month.MAY,2),
                repeatPeriod = RepeatPeriod.NEVER,
                daysOfWeek = arrayListOf(DayOfWeek.MONDAY),
                enabled = false,
                incompatible = false
            ))

        return eventList

    }

    private suspend fun getEventFromWatch(eventNumber: Int): Event {

        return Event(
            title = "Event 10",
            startDate = EventDate(2024, Month.MAY, 1), // Replace with actual EventDate structure
            endDate = EventDate(2024,Month.MAY,2),
            repeatPeriod = RepeatPeriod.NEVER,
            daysOfWeek = arrayListOf(DayOfWeek.MONDAY),
            enabled = false,
            incompatible = false
        )
    }

    fun setEvents(events: ArrayList<Event>) {
        // NO-OP
    }

    fun clearEvents() {
        // NO-OP
    }

    suspend fun getSettings(): Settings {
        val stetting = Settings()
        return stetting
    }

    private suspend fun getBasicSettings(): Settings {
        return Settings()
    }

    private suspend fun getTimeAdjustment(): TimeAdjustmentInfo {
        return TimeAdjustmentInfo(true, 30)
    }

    fun setSettings(settings: Settings) {
        // NO-OP
    }

    fun disconnect(context: Context) {
        // NO-OP
    }

    fun stopScan() {
        // NO-OP
    }

    fun isBluetoothEnabled(context: Context): Boolean {
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(message: String) {
       // NO-OP
    }

    fun resetHand() {
        // NO-OP
    }

    fun validateBluetoothAddress(deviceAddress: String?): Boolean {
        return true
    }

    fun preventReconnection(): Boolean {
        return true
    }
}