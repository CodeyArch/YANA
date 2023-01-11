package me.goobydev.composenotes.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.domain.use_case.NoteUseCases
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.NoteOrder
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.OrderType
import javax.inject.Inject

/* This is the main ViewModel for the notes screen and trash screen. It controls the events
 related to these two screens */
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var recentlyTrashedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote = event.note
                    noteUseCases.deleteNote(event.note)
                }
            }
            is NotesEvent.TrashNote -> {
                viewModelScope.launch {
                    recentlyTrashedNote = event.note
                    noteUseCases.deleteNote(event.note)
                    recentlyTrashedNote = recentlyTrashedNote?.copy(isTrashed = true)
                    noteUseCases.addNote(recentlyTrashedNote ?: return@launch)
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null

                }
            }
            is NotesEvent.RestoreTrash -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyTrashedNote?.copy(isTrashed = false) ?: return@launch)
                    recentlyTrashedNote = null
                }
            }
            is NotesEvent.RestoreTrashGlobal -> {
                viewModelScope.launch {
                    recentlyTrashedNote = event.note
                    noteUseCases.deleteNote(event.note)
                    noteUseCases.addNote(recentlyTrashedNote?.copy(isTrashed = false) ?: return@launch)
                    recentlyTrashedNote = null
                }

            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.ToggleSearchSection -> {
                _state.value = state.value.copy(
                    isSearchSectionVisible = !state.value.isSearchSectionVisible
                )
            }
            is NotesEvent.OpenNote -> {
                viewModelScope.launch {
                    _eventFlow.emit(UIEvent.LoadNote)
                }

            }
        }
    }
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
    sealed class UIEvent {
        object LoadNote: UIEvent()
    }
}