package me.goobydev.composenotes.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R.string
import me.goobydev.composenotes.core.presentation.components.DefaultRadioButton
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.util.OrderType
import me.goobydev.composenotes.feature_note.presentation.notes.components.NoteItem
import me.goobydev.composenotes.feature_note.presentation.notes.components.OrderSection
import me.goobydev.composenotes.feature_note.presentation.notes.components.SearchView
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_settings.data.SaveReadOnlyPreference
import me.goobydev.composenotes.feature_settings.data.SaveTrashedPreference


/* This is the main screen that users will see while using the notes application. This screen contains
note items that when clicked will navigate to the AddEditNoteScreen with the relevant noteId.
This screen allows users to create a new note or search for/order existing notes.
From this screen they can also delete/trash notes if needed. */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var maxLines by rememberSaveable { mutableStateOf(10) }
    val searchState = remember { mutableStateOf(TextFieldValue("")) } // Needs to be treated as mutableState therefore "=" not "by"
    val context = LocalContext.current
    val trashDataStore = SaveTrashedPreference(context)
    val currentTrashPreference = trashDataStore.getPreferences.collectAsState(initial = true)
    val readOnlyDataStore = SaveReadOnlyPreference(context)
    val currentReadOnlyPreference = readOnlyDataStore.getPreferences.collectAsState(initial = false)
    //TODO: State hoisting of settings preferences
    Scaffold(
        topBar = {
            NavTopAppBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = stringResource(string.sort),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleSearchSection)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }

            }

        },
        drawerContent = {
            NavDrawer(navController)
                        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          navController.navigate(Screen.AddEditNoteScreen.route)
                },
                backgroundColor = MaterialTheme.colors.onSurface
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(string.add_note),
                    tint = MaterialTheme.colors.surface
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            AnimatedVisibility(
                visible = state.isSearchSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SearchView(searchState)
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Text(
                        text = "Note size",
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        DefaultRadioButton(
                            text = "Large",
                            selected = maxLines == 10,
                            onSelect = {
                                maxLines = 10
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        DefaultRadioButton(
                            text = "Medium",
                            selected = maxLines == 5,
                            onSelect = {
                                maxLines = 5
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        DefaultRadioButton(
                            text = "Small",
                            selected = maxLines == 1,
                            onSelect = {
                                maxLines = 1
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sort",
                        style = MaterialTheme.typography.h6
                    )
                    OrderSection(
                        noteOrder = state.noteOrder,
                        onOrderChange = {
                            viewModel.onEvent((NotesEvent.Order(it)))
                        },
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.notes) { note ->
                    if (!note.isTrashed) {
                        val searchedText = searchState.value.text
                        if (
                            note.content.contains(searchedText, ignoreCase = true) ||
                            note.title.contains(searchedText, ignoreCase = true) ||
                            note.content.contains(searchedText, ignoreCase = true) &&
                            note.title.contains(searchedText, ignoreCase = true)) {
                            NoteItem(
                                note = note,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            Screen.AddEditNoteScreen.route +
                                                    "?noteId=${note.id}&noteColour=${note.backgroundColour}&readOnly=${currentReadOnlyPreference.value}"
                                            // navigate to the correct note with the correct background
                                        )
                                    },
                                onDeleteClick = {
                                    if(currentTrashPreference.value == true) {
                                        viewModel.onEvent(NotesEvent.TrashNote(note))
                                        scope.launch {
                                            // Shows the snackbar for every note deletion, can cause spam if
                                            // many notes are deleted in quick succession
                                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                                message = "Note archived: Find on 'Archive' page",
                                                actionLabel = "Undo"
                                            )
                                            if( result == SnackbarResult.ActionPerformed) {
                                                viewModel.onEvent(NotesEvent.RestoreTrash)
                                            }
                                        }
                                    } else {
                                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                                        scope.launch {
                                            // Shows the snackbar for every note deletion, can cause spam if
                                            // many notes are deleted in quick succession
                                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                                message = "Note Deleted",
                                                actionLabel = "Undo"
                                            )
                                            if( result == SnackbarResult.ActionPerformed) {
                                                viewModel.onEvent(NotesEvent.RestoreNote)
                                            }
                                        }
                                    }
                                     },
                                maxLines = maxLines,
                                isTrashed = false,
                                onRestoreClick = { viewModel.onEvent(NotesEvent.RestoreTrash) }
                            )
                        }
                    }
                    }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }

}