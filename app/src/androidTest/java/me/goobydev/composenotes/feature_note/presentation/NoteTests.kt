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
import me.goobydev.composenotes.feature_note.presentation.add_edit_note.AddEditNoteScreen
import me.goobydev.composenotes.feature_note.presentation.main.MainActivity
import me.goobydev.composenotes.feature_note.presentation.notes.NotesScreen
import me.goobydev.composenotes.feature_note.presentation.util.Screen
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
                }
            }
        }
    }

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


    @Test
    fun saveNewNotes_orderByTitleDescending() {
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
            .onNodeWithTag("Color(0.0, 0.0, 0.0, 1.0, sRGB IEC61966-2.1)")
            .assertIsDisplayed()
            .performClick()
        composeRule
            .onNodeWithTag("TEXT_COLOUR_BUTTON")
            .assertIsDisplayed()
            .performClick()
        composeRule
            .onNodeWithTag("Color(1.0, 1.0, 1.0, 1.0, sRGB IEC61966-2.1)")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        composeRule.onNodeWithText("test-title").assertIsDisplayed()
    }
}