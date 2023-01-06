package me.goobydev.composenotes.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

/* AddEditNoteEvents used in the AddEditNoteViewModel */
sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()

    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()

    data class ChangeBackgroundColour(val colour: Int): AddEditNoteEvent()
    data class ChangeTextColour(val colour: Int): AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
    object SaveNoteWithoutExit: AddEditNoteEvent()
}
