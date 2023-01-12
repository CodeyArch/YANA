package me.goobydev.composenotes.feature_note.presentation

import android.app.Application
import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import me.goobydev.composenotes.di.AppModule
import me.goobydev.composenotes.feature_misc_screens.presentation.trash.TrashScreen
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.AddEditNoteScreen
import me.goobydev.composenotes.feature_note.presentation.main.MainActivity
import me.goobydev.composenotes.feature_note.presentation.notes.NotesScreen
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.feature_settings.presentation.settings.NoteEditingSettingsScreen
import me.goobydev.composenotes.feature_settings.presentation.settings.SettingsScreen
import me.goobydev.composenotes.ui.theme.ComposeNotesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test


// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}

// Basic tests to check crucial functionality works on the app
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NoteTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            ComposeNotesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColour={noteColour}&readOnly={readOnly}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColour"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "readOnly"
                            ) {
                                type = NavType.BoolType
                                defaultValue = false
                            }

                        )
                    ) {
                        val colour = it.arguments?.getInt("noteColour") ?: -1
                        val readOnly = it.arguments?.getBoolean("readOnly") ?: false
                        AddEditNoteScreen(
                            navController = navController,
                            noteColour = colour,
                            readOnly = readOnly
                        )
                    }
                    composable(route = Screen.SettingsScreen.route) {
                        SettingsScreen(navController = navController)
                    }
                    composable(route = Screen.NoteEditingSettingsScreen.route) {
                        NoteEditingSettingsScreen(navController = navController)
                    }
                    composable(route = Screen.TrashedNotesScreen.route) {
                        TrashScreen(navController = navController)
                    }
                }
            }
        }
    }

    /* This test works by creating a note with a title and some content, confirming the note
    * exists on the home page, then opening the note again and editing the title and saving it.
    * It then confirms that the edit was saved correctly on the home page */
    @Test
    fun saveNewNote_EditAfter() {
        composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag("CONTENT_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-content")
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        composeRule.onNodeWithText("test-title").performClick()

        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .assertIsDisplayed()
            .assertTextEquals("test-title")
        composeRule
            .onNodeWithTag("CONTENT_SECTION")
            .assertIsDisplayed()
            .assertTextEquals("test-content")
        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .performTextInput("test-title2")
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onNodeWithText("test-title2").assertIsDisplayed()
    }

    /* This test works by creating 3 notes, all with different and content.
    * It then confirms the existence of all 3 notes and then checks they are displayed correctly
    * when sorted by title ascending. After that, it will then check they are displayed correctly
    * when sorted by title descending */
    @Test
    fun saveNewNotes_orderByTitle() {
        for(i in 1..3) {
            composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

            composeRule
                .onNodeWithTag("TITLE_SECTION")
                .assertIsDisplayed()
                .performTextInput("$i")
            composeRule
                .onNodeWithTag("CONTENT_SECTION")
                .assertIsDisplayed()
                .performTextInput("$i")
            composeRule.onNodeWithContentDescription("Save Note").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Ascending")
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertTextContains("1")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[2]
            .assertTextContains("3")

        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[2]
            .assertTextContains("1")
    }

    /* This test works by creating 3 notes, all with different and content.
    * It then confirms the existence of all 3 notes and then checks they are displayed correctly
    * when sorted by time ascending. After that, it will then check they are displayed correctly
    * when sorted by time descending */
    @Test
    fun saveNewNotes_orderByTime() {
        for(i in 1..3) {
            composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

            composeRule
                .onNodeWithTag("TITLE_SECTION")
                .assertIsDisplayed()
                .performTextInput("$i")
            composeRule
                .onNodeWithTag("CONTENT_SECTION")
                .assertIsDisplayed()
                .performTextInput("$i")
            composeRule.onNodeWithContentDescription("Save Note").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Time")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Ascending")
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertTextContains("1")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[2]
            .assertTextContains("3")

        composeRule
            .onNodeWithContentDescription("Time")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag("NOTE_ITEM")[2]
            .assertTextContains("1")
    }


    /* This test will only work assuming the screen is portrait due to the fact that background
    * colour and text colour buttons aren't available in landscape
    *
    * There is also the issue that colour changes aren't automatically validated, only the button
    * press itself is. There is currently no way to check the colour of a component so this
    * test requires manual observation */
    @Test
    fun saveNewNote_EditColours() {
        composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag("CONTENT_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-content")
        composeRule
            .onNodeWithTag("BACKGROUND_COLOUR_BUTTON")
            .assertIsDisplayed()
            .performClick()
        composeRule
            .onAllNodesWithTag("BACKGROUND_COLOUR_OPTION")[0] // Black
            .assertIsDisplayed()
            .performClick()
        composeRule
            .onNodeWithTag("TEXT_COLOUR_BUTTON")
            .assertIsDisplayed()
            .performClick()
        composeRule
            .onAllNodesWithTag("TEXT_COLOUR_OPTION")[12] // White
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onNodeWithText("test-title").assertIsDisplayed()
    }

    /* This test exists to check the archival feature is working as expected
    *
    * This test works by checking the archive page is empty, going back to the home screen
    * making a new note and saving it, confirming the existence of the note and deleting it,
    * confirming the note is gone and then checking it is on the archive page and
    * confirming the note exists. It then restores the note from the archive page,
    * confirms it has disappeared and then checks for it on the
    * home page and confirming its existence. */
    @Test
    fun noteArchiveAndUnarchive() {
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("ARCHIVE")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag("CONTENT_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-content")
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Delete Note")[0].performClick()
        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("ARCHIVE")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Restore Note")[0].performClick()
        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()

    }

    /* This test follows the same process as noteArchiveAndUnarchive() until
    it gets to the settings page and disables the archive setting that is enabled by default.
     After that, it will delete the note again and check that it did not get archived */
    @Test
    fun noteArchiveSettingTest() {
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("ARCHIVE")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithContentDescription("Add Note").assertExists().performClick()

        composeRule
            .onNodeWithTag("TITLE_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag("CONTENT_SECTION")
            .assertIsDisplayed()
            .performTextInput("test-content")
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Delete Note")[0].performClick()
        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("ARCHIVE")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Restore Note")[0].performClick()
        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("SETTINGS")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithTag("MOVE_DELETED_NOTES_TO_ARCHIVE")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("Delete Note")[0].performClick()
        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("ARCHIVE")
            .assertIsDisplayed()
            .performClick()

        composeRule.onAllNodesWithTag("NOTE_ITEM")[0]
            .assertDoesNotExist()

        /* This part is necessary because the test device can sometimes persist
        the settings change between tests. Resetting it back to what it was before
        allows for consistent success */
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("SETTINGS")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithTag("MOVE_DELETED_NOTES_TO_ARCHIVE")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag("NAV_DRAWER_BUTTON")
            .assertExists()
            .performClick()
        composeRule.onNodeWithTag("HOME")
            .assertIsDisplayed()
            .performClick()

    }
    // Add tests for checking how settings affect note archival
}