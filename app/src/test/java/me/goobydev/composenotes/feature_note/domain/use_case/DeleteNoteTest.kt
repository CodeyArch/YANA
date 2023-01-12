package me.goobydev.composenotes.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.goobydev.composenotes.feature_note.data.repository.FakeNoteRepository
import me.goobydev.composenotes.feature_note.domain.model.Note
import org.junit.Before
import org.junit.Test

class DeleteNoteTest {
    private lateinit var deleteNote: DeleteNote
    private lateinit var fakeRepository: FakeNoteRepository
    private lateinit var note: Note

    @Before
    fun setUp() {
        fakeRepository = FakeNoteRepository()
        deleteNote = DeleteNote(fakeRepository)
        note = Note(
            title = "title",
            content = "content",
            timestamp = 100_000L,
            backgroundColour = 1,
            textColour = 1,
            isTrashed = false,
            id = 1
        )
        runBlocking {
            fakeRepository.insertNote(note)
        }
    }

    @Test
    fun `delete note with valid ID`() = runBlocking {
        assertThat(fakeRepository.getNoteById(1)).isNotNull()
        val note = fakeRepository.getNoteById(1)
        if (note != null) {
            deleteNote(
                note
            )
        }
        assertThat(fakeRepository.getNoteById(1)).isNull()
    }
}