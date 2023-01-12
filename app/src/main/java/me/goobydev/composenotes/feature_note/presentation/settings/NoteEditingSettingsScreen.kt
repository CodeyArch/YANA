package me.goobydev.composenotes.feature_settings.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.core.presentation.components.BackTopAppBar
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_note.data.preferences.SaveAutosavePreferences
import me.goobydev.composenotes.feature_note.data.preferences.SaveReadOnlyPreference
import me.goobydev.composenotes.feature_note.data.preferences.SaveSaveOnBackPressPreferences
import me.goobydev.composenotes.feature_note.presentation.settings.components.SettingSwitch

/* The screen that holds settings related specifically to note editing such as autosaving
and read only*/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditingSettingsScreen(
    navController: NavController,
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //TODO: State hoisting of settings preferences
    val readOnlyDataStore = SaveReadOnlyPreference(context)
    val currentReadOnlyPreference = readOnlyDataStore.getPreferences.collectAsState(initial = false)
    val saveOnBackPressDataStore = SaveSaveOnBackPressPreferences(context)
    val currentSaveOnBackPressPreferences = saveOnBackPressDataStore.getPreferences.collectAsState(initial = true)
    val saveAutosaveDataStore = SaveAutosavePreferences(context)
    val currentAutosavePreferences = saveAutosaveDataStore.getPreferences.collectAsState(initial = true)

    Scaffold(
        topBar = {
            BackTopAppBar {
                navController.navigate(route = Screen.SettingsScreen.route)
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SettingSwitch(
                title = "Open notes in read only",
                contentDescription = "Notes will be opened in a read only state if enabled",
                onClick = {
                    val reverseValue = !currentReadOnlyPreference.value!!
                    scope.launch {
                        readOnlyDataStore.savePreferences(reverseValue)
                    }
                },
                checked = currentReadOnlyPreference.value == true)
            Spacer(modifier = Modifier.height(12.dp))
            SettingSwitch(
                title = "Save notes on back press",
                contentDescription = "On a back press, notes will automatically save if enabled. " +
                        "Disabling this could be risky for your notes as they may require manual saves using the save icon",
                onClick = {
                    val reverseValue = !currentSaveOnBackPressPreferences.value!!
                    scope.launch {
                        saveOnBackPressDataStore.savePreferences(reverseValue)
                    }
                },
                checked = currentSaveOnBackPressPreferences.value == true)
            Spacer(modifier = Modifier.height(12.dp))
            SettingSwitch(
                title = "Autosave notes after edit",
                contentDescription = "After edit to note content, notes will autosave if enabled. " +
                        "Disabling this could be risky for your notes as they may require manual saves using the save icon",
                onClick = {
                    val reverseValue = !currentAutosavePreferences.value!!
                    scope.launch {
                        saveAutosaveDataStore.savePreferences(reverseValue)
                    }
                },
                checked = currentAutosavePreferences.value == true)
        }
    }

}