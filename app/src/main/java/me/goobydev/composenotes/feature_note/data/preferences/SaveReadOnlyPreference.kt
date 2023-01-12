package me.goobydev.composenotes.feature_note.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* Datastore that Saves and Gets preferences related to Read Only */
class SaveReadOnlyPreference(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("readOnlyPreference")
        val USER_READ_ONLY_KEY = booleanPreferencesKey("read_only_preference")
    }
    val getPreferences: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_READ_ONLY_KEY] ?: false
        }
    suspend fun savePreferences(preference: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_READ_ONLY_KEY] = preference
        }
    }
}