package me.goobydev.composenotes.feature_note.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import me.goobydev.composenotes.feature_misc_screens.presentation.about.AboutScreen
import me.goobydev.composenotes.feature_misc_screens.presentation.clicker_screen.ClickerScreen
import me.goobydev.composenotes.feature_misc_screens.presentation.help.HelpScreen
import me.goobydev.composenotes.feature_misc_screens.presentation.trash.TrashScreen
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.AddEditNoteScreen
import me.goobydev.composenotes.feature_note.presentation.notes.NotesScreen
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_settings.presentation.settings.NoteEditingSettingsScreen
import me.goobydev.composenotes.feature_settings.presentation.settings.SettingsScreen
import me.goobydev.composenotes.ui.theme.ComposeNotesTheme

/* The entry point for the application and where the notes theme is applied. Navigation is created here
and all screens on the app will be listed and referenced here */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposeNotesTheme {
                Surface (
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable (
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColour={noteColour}&readOnly={readOnly}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColour"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "readOnly"
                                ) {
                                    type = NavType.BoolType
                                    defaultValue = false
                                }

                            )
                        ) {
                            val colour = it.arguments?.getInt("noteColour") ?: -1
                            val readOnly = it.arguments?.getBoolean("readOnly") ?: false
                            AddEditNoteScreen(
                               navController = navController,
                               noteColour = colour,
                                readOnly = readOnly
                           )
                        }
                        composable(route = Screen.SettingsScreen.route) {
                            SettingsScreen(navController = navController)
                        }
                        composable(route = Screen.NoteEditingSettingsScreen.route) {
                            NoteEditingSettingsScreen(navController = navController)
                        }
                        composable(route = Screen.HelpScreen.route) {
                            HelpScreen(navController = navController)
                        }
                        composable(route = Screen.AboutScreen.route) {
                            AboutScreen(navController = navController)
                        }
                        composable(route = Screen.ClickerEasterEggScreen.route) {
                            ClickerScreen(navController = navController)
                        }
                        composable(route = Screen.TrashedNotesScreen.route) {
                            TrashScreen(navController = navController)
                        }

                    }
                }
            }
        }
    }
}

