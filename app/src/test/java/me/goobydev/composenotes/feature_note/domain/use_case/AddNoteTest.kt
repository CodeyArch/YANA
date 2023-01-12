package me.goobydev.composenotes.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.goobydev.composenotes.feature_note.data.repository.FakeNoteRepository
import me.goobydev.composenotes.feature_note.domain.model.InvalidNoteException
import me.goobydev.composenotes.feature_note.domain.model.Note
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var addNote: AddNote
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeRepository = FakeNoteRepository()
        addNote = AddNote(fakeRepository)
    }

    @Test(expected = InvalidNoteException::class )
    fun `Check note throws exception on invalid title`() = runBlocking {
        addNote(
            Note(
                title = "",
                content = "content",
                timestamp = 100_000L,
                backgroundColour = 1,
                textColour = 1,
                isTrashed = false,
                id = 1
            )
        )
        assertThat(fakeRepository.getNoteById(1)).isNull()
    }

    @Test(expected = InvalidNoteException::class )
    fun `Check note throws exception on invalid content`() = runBlocking {
        addNote(
            Note(
                title = "title",
                content = "",
                timestamp = 100_000L,
                backgroundColour = 1,
                textColour = 1,
                isTrashed = false,
                id = 1
            )
        )
        assertThat(fakeRepository.getNoteById(1)).isNull()
    }

    @Test(expected = InvalidNoteException::class )
    fun `Check note throws exception on invalid title and content`() = runBlocking {
        addNote(
            Note(
                title = "",
                content = "",
                timestamp = 100_000L,
                backgroundColour = 1,
                textColour = 1,
                isTrashed = false,
                id = 1
            )
        )
        assertThat(fakeRepository.getNoteById(1)).isNull()
    }

    @Test()
    fun `Check note accepts valid note`() = runBlocking {
        addNote(
            Note(
                title = "title",
                content = "content",
                timestamp = 100_000L,
                backgroundColour = 1,
                textColour = 1,
                isTrashed = false,
                id = 1
            )
        )
        assertThat(fakeRepository.getNoteById(1)).isNotNull()
    }
}