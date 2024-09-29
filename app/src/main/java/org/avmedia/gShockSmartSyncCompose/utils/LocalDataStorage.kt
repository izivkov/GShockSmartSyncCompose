package org.avmedia.gShockSmartSyncCompose.utils

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object LocalDataStorage {

    private const val STORAGE_NAME = "CASIO_GOOGLE_SYNC_STORAGE"
    private val scope = CoroutineScope(Dispatchers.IO)

    // Use a DataStore delegate in context
    private val Context.dataStore by preferencesDataStore(
        name = STORAGE_NAME,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, STORAGE_NAME))
        }
    )

    fun put(context: Context, key: String, value: String) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    fun get(context: Context, key: String, defaultValue: String? = null): String? {
        var value: String?
        runBlocking {
            val preferences = context.dataStore.data.first()
            value = preferences[stringPreferencesKey(key)] ?: defaultValue
        }
        return value
    }

    fun delete(context: Context, key: String) {
        scope.launch {
            deleteAsync(context, key)
        }
    }

    suspend fun deleteAsync(context: Context, key: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    private fun getBoolean(context: Context, key: String): Boolean {
        return get(context, key, "false")?.toBoolean() ?: false
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        scope.launch {
            put(context, key, value.toString())
        }
    }

    fun getTimeAdjustmentNotification(context: Context): Boolean {
        return getBoolean(context, "timeAdjustmentNotification")
    }

    fun setTimeAdjustmentNotification(context: Context, value: Boolean) {
        putBoolean(context, "timeAdjustmentNotification", value)
    }

    fun getTimeAdjustmentTimeOffsetMs(context: Context): String? {
        return get(context, "timeOffsetMs")
    }

    fun setTimeAdjustmentTimeOffsetMs(context: Context, value: String) {
        put(context, "timeOffsetMs", value)
    }

    fun getMirrorPhoneDnd(context: Context): Boolean {
        return getBoolean(context, "mirrorPhoneDnD")
    }

    fun setMirrorPhoneDnD(context: Context, value: Boolean) {
        putBoolean(context, "mirrorPhoneDnD", value)
    }

    fun getKeepAlive(context: Context): Boolean {
        return getBoolean(context, "keepAlive")
    }

    fun setKeepAlive(context: Context, value: Boolean) {
        putBoolean(context, "keepAlive", value)
    }

    fun getAllData(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            val allEntries = preferences.asMap()
            val stringBuilder = StringBuilder()
            allEntries.forEach { (key, value) ->
                stringBuilder.append("$key: $value\n")
            }
            stringBuilder.toString()
        }
    }

    fun getAutoLightNightOnly(context: Context): Boolean {
        return getBoolean(context, "autoLightNightOnly")
    }

    fun setAutoLightNightOnly(context: Context, value: Boolean) {
        putBoolean(context, "autoLightNightOnly", value)
    }
}
