package me.goobydev.composenotes.feature_note.presentation.notes

import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.NoteOrder
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.OrderType
/* data class for the purpose of holding the state of the notes screen, including the list of notes,
the order type, the visibility of the order section and the visibility of the search section. */
data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isSearchSectionVisible: Boolean = false
)
