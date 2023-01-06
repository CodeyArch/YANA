package me.goobydev.composenotes.feature_settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* Datastore that Saves and Gets preferences related to Theme Selection */
class SaveUserTheme(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userTheme")
        val USER_THEME_KEY= stringPreferencesKey("user_theme")
    }
    val getTheme: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_THEME_KEY] ?: "System"
        }
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_THEME_KEY] = theme
        }
    }
}