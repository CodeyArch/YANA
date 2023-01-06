package me.goobydev.composenotes.feature_note.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.goobydev.composenotes.feature_note.domain.model.Note

/* This data access object exists to interact with the notes database for the functionality of the app */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}