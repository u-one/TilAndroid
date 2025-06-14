package net.uoneweb.android.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ReceiptSettingDataStore(context: Context) {
    private val OPEN_API_KEY = stringPreferencesKey("open_api_key")

    suspend fun saveOpenApiKey(apiKey: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[OPEN_API_KEY] = apiKey
        }
    }

    val apiKeyFlow: Flow<String> = context.dataStore.data
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
}

private const val PREFERENCES_NAME = "receipt_preference"

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME,
)