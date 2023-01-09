package me.goobydev.composenotes.feature_misc_screens.presentation.help

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar
import me.goobydev.composenotes.feature_misc_screens.presentation.components.InfoDropdown
import me.goobydev.composenotes.feature_note.presentation.util.Screen

/* A help screen to explain the apps functions and capabilities to the user.
This screen also houses the access to the clicker easter egg */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HelpScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            NavTopAppBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        drawerContent = { NavDrawer(navController) },
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item{
                InfoDropdown(title = "How to make, edit and delete notes") {
                    Text("""
                        You can create notes using the + (plus) button located in the bottom right on the notes page.
                        
                        You can customise your new notes background with the circular colour button on the top center of the new note page and you can customise your new notes text colour with the circular Aa button in the same row.
                        
                        Notes are saved automatically when you exit the screen with a back press and you can save them manually with the save icon located in the bottom right of the new notes page.
                        
                        You can open any note from your notes page by tapping on the corresponding note item
                        
                        Notes can be deleted with the trash icon found on the bottom right corner of a note. Deleting a note is permanent but you can undo the deletion for a few seconds after deleting. If you do not undo, that note will be lost forever
                    """.trimIndent(),
                        style = MaterialTheme.typography.body1)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            item{
                InfoDropdown(title = "Where notes are stored") {
                    Text("""
                        The notes on this app are stored locally in the apps internal storage. Notes cannot be accessed from outside the application and you will not be able to view them using a file manager or by connecting to a computer. This helps to keep your data secure and grant you privacy.
                        
                        There is currently no option to backup notes so if you uninstall the application, your notes may be lost forever. A backup option is planned for future updates but as of current it is not available.
                    """.trimIndent(),
                        style = MaterialTheme.typography.body1)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item{
                InfoDropdown(title = "How to change the font size") {
                    Text("""
                        There is no current in-app option to adjust your font size, however, font size can be adjusted through your device's settings if you wish to do so. An in-app option may be implemented in a future update.
                    """.trimIndent(),
                        style = MaterialTheme.typography.body1)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item{
                InfoDropdown(title = "How to change the font type") {
                    Text("""
                        By default, this application will use your device system font as the main font for text displayed on this app. 
                        
                        If you wish to change your in-app font, you can find a selection of fonts on the settings page using the "Font" button. Tap any of these to change your font type.
                        
                        If you have the font type set to system, you change your app font by changing your device font.
                    """.trimIndent(),
                        style = MaterialTheme.typography.body1)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item{
                InfoDropdown(title = "How to change the app theme") {
                    Text("""
                        By default, this application will use your device system theme as the theme displayed on this app. 
                        
                        If you wish to change your in-app theme, you can find a selection of themes on the settings page using the "Themes" button. Tap any of these to change your theme.
                        
                        If you have the theme type set to system, you change your app theme by changing your device theme.
                    """.trimIndent(),
                        style = MaterialTheme.typography.body1)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}