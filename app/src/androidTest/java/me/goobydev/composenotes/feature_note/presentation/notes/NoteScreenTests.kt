package me.goobydev.composenotes.feature_note.presentation.notes

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import me.goobydev.composenotes.di.AppModule
import me.goobydev.composenotes.feature_note.presentation.main.MainActivity
import me.goobydev.composenotes.feature_note.presentation.util.Screen
import me.goobydev.composenotes.ui.theme.ComposeNotesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            ComposeNotesTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun orderSectionAllButtons() {
        composeRule.onNodeWithTag("ORDER_SECTION").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag("ORDER_SECTION").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Large").assertExists().performClick().assertIsSelected()
        composeRule.onNodeWithContentDescription("Medium").assertExists().performClick().assertIsSelected()
        composeRule.onNodeWithContentDescription("Small").assertExists().performClick().assertIsSelected()

        composeRule.onNodeWithContentDescription("Title").assertExists().performClick().assertIsSelected()
        composeRule.onNodeWithContentDescription("Time").assertExists().performClick().assertIsSelected()
        composeRule.onNodeWithContentDescription("Colour").assertExists().performClick().assertIsSelected()

        composeRule.onNodeWithContentDescription("Ascending").assertExists().performClick().assertIsSelected()
        composeRule.onNodeWithContentDescription("Descending").assertExists().performClick().assertIsSelected()

        composeRule.onNodeWithContentDescription("Sort").performClick()

    }

    @Test
    fun searchSectionFunctionTest() {
        composeRule.onNodeWithTag("SEARCH_SECTION").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithTag("SEARCH_SECTION").assertIsDisplayed()

        composeRule
            .onNodeWithTag("SEARCH_SECTION")
            .performTextInput("test-search")
        composeRule.onNodeWithText("test-search").assertIsDisplayed()
        composeRule
            .onNodeWithTag("EMPTY_SEARCH_BUTTON")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithText("test-search").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithTag("SEARCH_SECTION").assertDoesNotExist()
    }
}