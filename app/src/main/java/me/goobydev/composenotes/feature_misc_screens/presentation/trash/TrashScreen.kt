package me.goobydev.composenotes.feature_misc_screens.presentation.trash

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R
import me.goobydev.composenotes.core.presentation.components.DefaultRadioButton
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar
import me.goobydev.composenotes.feature_note.presentation.notes.NotesEvent
import me.goobydev.composenotes.feature_note.presentation.notes.NotesViewModel
import me.goobydev.composenotes.feature_note.presentation.notes.components.NoteItem
import me.goobydev.composenotes.feature_note.presentation.notes.components.OrderSection
import me.goobydev.composenotes.feature_note.presentation.notes.components.SearchView
import me.goobydev.composenotes.feature_note.presentation.util.Screen

/*
This page is essentially a copy of the standard notes page except:
 - Notes deleted here are deleted permanently
 - No new notes can be created
 - Notes are read only
If the user has trash disabled, no notes will be posted here
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TrashScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var maxLines by rememberSaveable { mutableStateOf(10) }
    val searchState = remember { mutableStateOf(TextFieldValue("")) } // Needs to be treated as mutableState therefore "=" not "by"

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
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = stringResource(R.string.sort),
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
                    if (note.isTrashed) {
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
                                                    "?noteId=${note.id}&noteColour=${note.backgroundColour}&readOnly=${true}"
                                            // navigate to the correct note with the correct background
                                        )
                                    },
                                onDeleteClick = {
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
                                    } },
                                maxLines = maxLines,
                                isTrashed = true,
                                onRestoreClick = {
                                    viewModel.onEvent(NotesEvent.RestoreTrashGlobal(note))
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Note restored"
                                        )
                                    }
                                }
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