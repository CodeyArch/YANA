package me.goobydev.composenotes.feature_settings.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_settings.data.SaveTrashedPreference
import me.goobydev.composenotes.feature_settings.presentation.components.FontSelect
import me.goobydev.composenotes.feature_settings.presentation.components.SettingOpen
import me.goobydev.composenotes.feature_settings.presentation.components.SettingSwitch
import me.goobydev.composenotes.feature_settings.presentation.components.ThemeSelect

/* The settings screen which holds the main important settings and a link to other settings screens.
Currently the only screen it links to is the NoteEditingSettingsScreen as other settings
do not exist */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var themeSelectOpen by rememberSaveable { mutableStateOf(false) }
    var fontSelectOpen by rememberSaveable { mutableStateOf(false) }

    //TODO: State hoisting of settings preferences
    val context = LocalContext.current
    val trashDataStore = SaveTrashedPreference(context)
    val currentTrashPreference = trashDataStore.getPreferences.collectAsState(initial = true)

    Scaffold(
        topBar = {
            NavTopAppBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        drawerContent = { NavDrawer(navController) },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SettingOpen(
                title = stringResource(R.string.theme),
                contentDescription = stringResource(R.string.theme_setting_description),
                onClick = { themeSelectOpen = true }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingOpen(
                title = "Font",
                contentDescription = "Changes default font of text elements",
                onClick = { fontSelectOpen = true }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingSwitch(
                title = "Move deleted notes to archive",
                contentDescription = "Deleted notes will be found in the archive page",
                onClick = {
                    val reverseValue = !currentTrashPreference.value!!
                    scope.launch {
                        trashDataStore.savePreferences(reverseValue)
                    }
                },
                checked = currentTrashPreference.value == true)
            Spacer(modifier = Modifier.height(12.dp))
            SettingOpen(
                title = "Note editing",
                contentDescription = "Opens page with note editing settings",
                onClick = { navController.navigate(route = Screen.NoteEditingSettingsScreen.route) }
            )
        }
    }
    if (themeSelectOpen) {
        ThemeSelect(onDismiss = { themeSelectOpen = false })
    }
    if (fontSelectOpen) {
        FontSelect(onDismiss = { fontSelectOpen = false })
    }

}
