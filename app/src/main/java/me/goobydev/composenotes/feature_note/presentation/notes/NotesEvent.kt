package me.goobydev.composenotes.feature_note.presentation.notes

import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.NoteOrder

/* Sealed class for events that occur on the notes/trash pages  */
sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class TrashNote(val note: Note): NotesEvent()
    data class RestoreTrashGlobal(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object RestoreTrash: NotesEvent()
    object ToggleOrderSection: NotesEvent()
    object ToggleSearchSection: NotesEvent()
    object OpenNote: NotesEvent()
}
