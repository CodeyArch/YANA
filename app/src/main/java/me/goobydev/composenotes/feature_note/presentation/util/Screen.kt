package me.goobydev.composenotes.feature_note.presentation.util

// Routes for all screens in the application
sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
    object SettingsScreen: Screen("settings_screen")
    object HelpScreen: Screen("help_screen")
    object AboutScreen: Screen("about_screen")
    object TrashedNotesScreen: Screen("trashed_screen")
    object NoteEditingSettingsScreen: Screen("note_editing_settings_screen")
}
