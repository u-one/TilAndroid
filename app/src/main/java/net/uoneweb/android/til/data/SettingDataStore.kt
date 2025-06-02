package net.uoneweb.android.til.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class SettingsDataStore(context: Context) {
    private val SHOW_BOTTOM_BAR_KEY = booleanPreferencesKey("show_bottom_bar")
    private val OPEN_API_KEY = stringPreferencesKey("open_api_key")
    private var apiKey = ""

    suspend fun saveOpenApiKey(apiKey: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[OPEN_API_KEY] = apiKey
        }
    }

    val preferenceFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[OPEN_API_KEY] ?: ""
        }

    val showBottomBarFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SHOW_BOTTOM_BAR_KEY] == true
        }

    suspend fun saveShowBottomBar(show: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_BOTTOM_BAR_KEY] = show
        }
    }
}

private const val APP_PREFERENCES_NAME = "app_preference"

private val Context.dataStore by preferencesDataStore(
    name = APP_PREFERENCES_NAME,
)
