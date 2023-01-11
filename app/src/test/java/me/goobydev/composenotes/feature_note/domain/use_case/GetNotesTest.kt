package me.goobydev.composenotes.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.goobydev.composenotes.feature_note.data.repository.FakeNoteRepository
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.NoteOrder
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.OrderType
import org.junit.Before
import org.junit.Test

class GetNotesTest {
    private lateinit var getNotes: GetNotes
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeRepository = FakeNoteRepository()
        getNotes = GetNotes(fakeRepository)

        val notesToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    backgroundColour = index,
                    textColour = index,
                    isTrashed = false
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach { fakeRepository.insertNote(it) }
        }
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Colour(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].backgroundColour).isLessThan(notes[i+1].backgroundColour)
        }
    }

    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Colour(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].backgroundColour).isGreaterThan(notes[i+1].backgroundColour)
        }
    }
}