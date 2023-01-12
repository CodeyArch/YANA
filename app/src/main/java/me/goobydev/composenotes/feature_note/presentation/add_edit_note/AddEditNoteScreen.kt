package me.goobydev.composenotes.feature_note.presentation.add_edit_note

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.luminance
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R
import me.goobydev.composenotes.feature_note.domain.model.Note
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.components.BackPressIntercept
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.components.ExitWithoutSaving
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import me.goobydev.composenotes.feature_settings.data.SaveAutosavePreferences
import me.goobydev.composenotes.feature_settings.data.SaveSaveOnBackPressPreferences

/* The AddEditNoteScreen exists in order to allow the users to edit an existing note,
create a new note or view a note in read only. This screen is accessed through clicking a note item
or the floating action button on the home notes page */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen (
    navController: NavController,
    noteColour: Int,
    readOnly: Boolean,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()
    var backgroundColourPickerOpen by rememberSaveable { mutableStateOf(false) }
    var textColourPickerOpen by rememberSaveable { mutableStateOf(false) }
    var exitWithoutSaveOpen by rememberSaveable { mutableStateOf(false) }

    //TODO: State hoisting of settings preferences
    val context = LocalContext.current
    val saveOnBackPressDataStore = SaveSaveOnBackPressPreferences(context)
    val currentSaveOnBackPressPreferences = saveOnBackPressDataStore.getPreferences.collectAsState(initial = true)
    val saveAutosaveDataStore = SaveAutosavePreferences(context)
    val currentAutosavePreferences = saveAutosaveDataStore.getPreferences.collectAsState(initial = true)

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if(noteColour != -1) noteColour else viewModel.noteColour.value)
        )
    } // TODO: Doesn't survive configuration changes
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message // Errors with saving
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }
    if(!readOnly) {
        if(currentSaveOnBackPressPreferences.value == true) {
            BackPressIntercept(onBackPressed = { viewModel.onEvent(AddEditNoteEvent.SaveNote) })
        }
    }

    Scaffold(
        // Save button
        floatingActionButton = {
            if (!readOnly) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditNoteEvent.SaveNote)
                    },
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.save_note),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            val configuration = LocalConfiguration.current
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp) // TODO: Hard coded, change for different screen sizes
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface)
                            .border(
                                width = 3.dp,
                                color = MaterialTheme.colors.surface,
                                shape = CircleShape
                            )
                            .clickable {
                                if (!readOnly) {
                                    exitWithoutSaveOpen = true
                                } else navController.navigateUp()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.exit_without_saving),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (!readOnly) {
                        // Background colour change button
                        Box(
                            modifier = Modifier
                                .size(50.dp) // TODO: Hard coded, change for different screen sizes
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color(viewModel.noteColour.value))
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.noteColour.value.luminance >= 0.1f) {
                                        Color.Black
                                    } else Color.White,
                                    shape = CircleShape
                                )
                                .clickable {
                                    backgroundColourPickerOpen = true
                                }
                                .testTag("BACKGROUND_COLOUR_BUTTON")
                        )
                        // Text colour change button
                        Box(
                            modifier = Modifier
                                .size(50.dp) // TODO: Hard coded, change for different screen sizes
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    color = if (viewModel.textColour.value.luminance >= 0.3f) {
                                        Color.Black
                                    } else Color.White
                                )
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.noteColour.value.luminance >= 0.1f) {
                                        Color.Black
                                    } else Color.White,
                                    shape = CircleShape
                                )
                                .clickable {
                                    textColourPickerOpen = true
                                }
                                .testTag("TEXT_COLOUR_BUTTON")
                        ) {
                            Text(
                                text = "Aa",
                                color = Color(viewModel.textColour.value),
                                modifier = Modifier.align(Alignment.Center),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section where users can edit the notes content and header
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                readOnly = readOnly,
                onValueChange = {
                    viewModel.onEvent((AddEditNoteEvent.EnteredTitle(it)))

                },
                onFocusChange = {
                    viewModel.onEvent((AddEditNoteEvent.ChangeTitleFocus(it)))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(viewModel.textColour.value),
                    fontStyle = MaterialTheme.typography.h6.fontStyle,
                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                    fontWeight = MaterialTheme.typography.h6.fontWeight,
                    fontSize = MaterialTheme.typography.h6.fontSize
                ),
                testTag = "TITLE_SECTION"
            )
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                readOnly = readOnly,
                onValueChange = {
                    viewModel.onEvent((AddEditNoteEvent.EnteredContent(it)))
                    //TODO: Extract to viewModel
                    if (currentAutosavePreferences.value == true) {
                        viewModel.onEvent(AddEditNoteEvent.SaveNoteWithoutExit)

                    }

                },
                onFocusChange = {
                    viewModel.onEvent((AddEditNoteEvent.ChangeContentFocus(it)))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = TextStyle(
                    color = Color(viewModel.textColour.value),
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = MaterialTheme.typography.body1.fontWeight,
                    fontSize = MaterialTheme.typography.body1.fontSize
                ),
                modifier = Modifier.fillMaxHeight(),
                testTag = "CONTENT_SECTION"
            )
        }
    }
    if (exitWithoutSaveOpen) {
        ExitWithoutSaving(
            onDismiss = { exitWithoutSaveOpen = false },
            onPositiveAction = {
                exitWithoutSaveOpen = false
                navController.navigateUp()
            }
        )
    }
    if (backgroundColourPickerOpen) {
        // Grid of note colour circles that users can click to change the background of notes
        val gridState = rememberLazyGridState()
        AlertDialog(
            shape = RoundedCornerShape(40.dp),
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(10.dp, vertical = 40.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            title = null,
            onDismissRequest = { backgroundColourPickerOpen = false },
            buttons = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(Note.mixedColoursV2) { colour ->
                        val colourInt = colour.toArgb()
                        println(colour.toString())
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(colour)
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.noteColour.value == colourInt) {
                                        Color.Black
                                    } else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBackgroundAnimatable.animateTo(
                                            targetValue = Color(colourInt),
                                            animationSpec = tween(
                                                durationMillis = 500
                                            )
                                        )
                                    }
                                    viewModel.onEvent(
                                        AddEditNoteEvent.ChangeBackgroundColour(
                                            colourInt
                                        )
                                    )
                                    backgroundColourPickerOpen =
                                        false // Closes the colour select menu
                                }
                                .testTag("BACKGROUND_COLOUR_OPTION")
                        )
                    }
                }
            }
        )

    }
    if (textColourPickerOpen) {
        // Grid of note colour circles that users can click to change the text colour of notes
        val gridState = rememberLazyGridState()
        AlertDialog(
            shape = RoundedCornerShape(40.dp),
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(10.dp, vertical = 40.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            title = null,
            onDismissRequest = { textColourPickerOpen = false },
            buttons = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(Note.mixedColoursV2) { colour ->
                        val colourInt = colour.toArgb()
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(colour)
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.textColour.value == colourInt) {
                                        Color.Black
                                    } else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    viewModel.onEvent(
                                        AddEditNoteEvent.ChangeTextColour(
                                            colourInt
                                        )
                                    )
                                    textColourPickerOpen =
                                        false // Closes the colour select menu
                                }
                                .testTag("TEXT_COLOUR_OPTION")
                        )
                    }
                }
            }
        )
    }
}