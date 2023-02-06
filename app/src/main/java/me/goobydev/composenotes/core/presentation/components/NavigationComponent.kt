package me.goobydev.composenotes.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.goobydev.composenotes.feature_misc_screens.presentation.about.AboutScreen
import me.goobydev.composenotes.feature_misc_screens.presentation.help.HelpScreen
import me.goobydev.composenotes.feature_note.presentation.archive.ArchiveScreen
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.AddEditNoteScreen
import me.goobydev.composenotes.feature_note.presentation.notes.NotesScreen
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_note.presentation.settings.NoteEditingSettingsScreen
import me.goobydev.composenotes.feature_note.presentation.settings.SettingsScreen

/* A composable function used for the main navigation for the entire application. It exists as a
* composable to make it reusable */
@Composable
fun NavigationComponent() {
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
                    name = "readOnly"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }

            )
        ) {
            val readOnly = it.arguments?.getBoolean("readOnly") ?: false
            AddEditNoteScreen(
                navController = navController,
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
        composable(route = Screen.ArchiveNotesScreen.route) {
            ArchiveScreen(navController = navController)
        }

    }
}