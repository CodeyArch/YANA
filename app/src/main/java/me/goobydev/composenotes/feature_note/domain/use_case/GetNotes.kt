package me.goobydev.composenotes.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.repository.NoteRepository
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.NoteOrder
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.OrderType

/* Use case to get all notes from the database and sort them in order of what is currently selected */
class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Colour -> notes.sortedBy { it.backgroundColour }
                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Colour -> notes.sortedByDescending { it.backgroundColour }
                    }
                }
            }
        }
    }
}