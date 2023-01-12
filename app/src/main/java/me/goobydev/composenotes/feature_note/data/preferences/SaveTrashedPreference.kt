package me.goobydev.composenotes.feature_note.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* Datastore that Saves and Gets preferences related to Trash/Archive */
class SaveTrashedPreference(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("trashPreference")
        val USER_TRASH_KEY = booleanPreferencesKey("trash_preference")
    }
    val getPreferences: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_TRASH_KEY] ?: true
        }
    suspend fun savePreferences(preference: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_TRASH_KEY] = preference
        }
    }
}