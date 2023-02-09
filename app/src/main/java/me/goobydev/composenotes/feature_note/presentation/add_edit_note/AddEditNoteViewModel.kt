package me.goobydev.composenotes.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.luminance
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.goobydev.composenotes.feature_note.data.preferences.SaveRandomNoteColourPreferences
import me.goobydev.composenotes.feature_note.domain.model.InvalidNoteException
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.use_case.NoteUseCases
import me.goobydev.composenotes.ui.theme.Black
import me.goobydev.composenotes.ui.theme.White
import javax.inject.Inject


/* ViewModel for the AddEditNoteScreen. The ViewModel is responsible for the events that occur on
this screen and pulls events from the AddEditNoteEvent Sealed Class. */
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    saveRandomNoteColourPreferences: SaveRandomNoteColourPreferences
) : ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter content..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private var randomNotePreference by mutableStateOf(true)

    init {
        viewModelScope.launch {
            randomNotePreference = saveRandomNoteColourPreferences.getPreferences.stateIn(viewModelScope).value!!
        }

    }
    private val _noteColour = mutableStateOf(
        if(randomNotePreference) {
            // Picks and entirely random colour from the colour options
            Note.mixedColoursV2.random().toArgb()
        } else {
            // Default is black due to the average user being a dark mode user.
            Black.toArgb()
        }
    )
    val noteColour: State<Int> = _noteColour

    // Sets a default text colour on start and makes sure it contrasts well with background
    private val _textColour = mutableStateOf( if (_noteColour.value.luminance > 0.3f){
        Black.toArgb()
    } else {
        White.toArgb()
    }
    )
    val textColour: State<Int> = _textColour

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    private var job: Job? = null

    init {

        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1 ) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColour.value = note.backgroundColour
                        _textColour.value = note.textColour
                    }
                    for (i in 0 until Note.mixedColoursV2.size) println(Note.mixedColoursV2[i])
                }
            } else currentNoteId = System.currentTimeMillis().toInt()
           /* ID for notes can be generated automatically by the database when notes are saved,
           however, autosaving of new notes becomes problematic if they are not assigned
           an ID on creation. A bug will occur in which multiple copies of the note are created.
           In future, the process of ID generation may change to be more foolproof   */

        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeBackgroundColour -> {
                _noteColour.value = event.colour
            }
            is AddEditNoteEvent.ChangeTextColour -> {
                _textColour.value = event.colour
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                backgroundColour = noteColour.value,
                                textColour = textColour.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.SaveNoteWithoutExit -> {
                if (currentNoteId != null) {
                    job?.cancel() // Stops the previous call of the event
                    job = viewModelScope.launch {
                        delay(2_000) // Delay of 2 seconds so that it does not save mid-edit
                        val note = Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            timestamp = System.currentTimeMillis(),
                            backgroundColour = noteColour.value,
                            textColour = textColour.value,
                            id = currentNoteId
                        )
                        try {
                            noteUseCases.addNote(
                                note
                            )
                        } catch(e: InvalidNoteException) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = e.message ?: "Couldn't save note"
                                )
                            )
                        }
                    }
                }
            }

        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}