package me.goobydev.composenotes.feature_note.presentation.add_edit_note

/* Simple data class for the state of a note text field, has hint set to true by default as hints
are required if the text is empty */
data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)