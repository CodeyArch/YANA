package me.goobydev.composenotes.feature_settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* Datastore that Saves and Gets preferences related to Font Selection */
class SaveUserFont(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userFont")
        val USER_FONT_KEY= stringPreferencesKey("user_font")
    }
    val getFont: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_FONT_KEY] ?: "System"
        }
    suspend fun saveFont(font: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_FONT_KEY] = font
        }
    }

}