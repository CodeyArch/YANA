package me.goobydev.composenotes.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.goobydev.composenotes.feature_note.data.repository.FakeNoteRepository
import me.goobydev.composenotes.feature_note.domain.model.Note
import org.junit.Before
import org.junit.Test

class GetNoteTest {
    private lateinit var getNote: GetNote
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeRepository = FakeNoteRepository()
        getNote = GetNote(fakeRepository)

        val notesToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    backgroundColour = index,
                    textColour = index,
                    isTrashed = false,
                    id = index
                )
            )
            notesToInsert.shuffle()
            runBlocking {
                notesToInsert.forEach { fakeRepository.insertNote(it) }
            }
        }
    }

    // Test to get a note by a known ID
    @Test
    fun `Get note by valid ID`() = runBlocking {
        assertThat(getNote(id = 0)).isNotNull()
    }

    // Test that it cannot get a note with an invalid ID
    @Test
    fun `Fail to get note by invalid ID`() = runBlocking {
        assertThat(getNote(id = 10000)).isNull()
    }
}