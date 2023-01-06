package me.goobydev.composenotes.feature_note.domain.use_case

import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.repository.NoteRepository

/* Use case to remove notes from the database */
class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}