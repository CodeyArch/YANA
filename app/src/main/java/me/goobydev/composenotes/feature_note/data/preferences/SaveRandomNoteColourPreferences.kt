package me.goobydev.composenotes.feature_note.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/* Datastore that Saves and Gets preferences related to random colours on notes */

class SaveRandomNoteColourPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("saveRandomNoteColourPreference")
        val USER_RANDOM_NOTE_COLOUR_KEY = booleanPreferencesKey("save_random_note_colour_preference")
    }
    val getPreferences: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_RANDOM_NOTE_COLOUR_KEY] ?: false
        }
    suspend fun savePreferences(preference: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_RANDOM_NOTE_COLOUR_KEY] = preference
        }
    }
}