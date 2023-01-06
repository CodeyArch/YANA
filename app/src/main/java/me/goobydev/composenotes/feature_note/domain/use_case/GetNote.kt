package me.goobydev.composenotes.feature_note.domain.use_case

import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.repository.NoteRepository
/* Use case to retrieve a single note from the database based on its ID */
class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}