package me.goobydev.composenotes.feature_note.domain.repository

import kotlinx.coroutines.flow.Flow
import me.goobydev.composenotes.feature_note.domain.model.Note

/* Functions to be used to interact with the database */
interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id:Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}