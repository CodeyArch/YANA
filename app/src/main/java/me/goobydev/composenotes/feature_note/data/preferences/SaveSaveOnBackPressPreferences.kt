package me.goobydev.composenotes.feature_note.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* Datastore that Saves and Gets preferences related to Save On Back Press */
class SaveSaveOnBackPressPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("saveOnBackPressPreference")
        val USER_SAVE_ON_BACK_PRESS_KEY = booleanPreferencesKey("save_on_back_press_preference")
    }
    val getPreferences: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_SAVE_ON_BACK_PRESS_KEY] ?: true
        }
    suspend fun savePreferences(preference: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_SAVE_ON_BACK_PRESS_KEY] = preference
        }
    }
}