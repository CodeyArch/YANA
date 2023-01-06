package me.goobydev.composenotes.feature_note.domain.use_case


import me.goobydev.composenotes.feature_note.domain.model.InvalidNoteException
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.repository.NoteRepository

/* Use case in which notes are added to the database, assuming the title and content are not blank */
class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("The title of the note cannot be empty.")
        }
        if(note.content.isBlank()) {
            throw InvalidNoteException("The content of the note cannot be empty,")
        }
        repository.insertNote(note)
    }
}