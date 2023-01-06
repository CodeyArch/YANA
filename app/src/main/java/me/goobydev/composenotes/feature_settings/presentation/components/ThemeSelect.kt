package me.goobydev.composenotes.feature_settings.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R
import me.goobydev.composenotes.core.presentation.components.DefaultRadioButton
import me.goobydev.composenotes.feature_settings.data.SaveUserTheme

/* A composable to allow users to select the global theme for the app. It takes and saves
preferences using the SaveUserTheme.kt preferences Datastore */
@Composable
fun ThemeSelect(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = SaveUserTheme(context)
    val currentTheme = dataStore.getTheme.collectAsState(initial = "")
    AlertDialog(
        shape = RoundedCornerShape(40.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(R.string.theme))
        },
        onDismissRequest =  onDismiss,
        buttons = {
            Column(modifier = Modifier
                .padding(16.dp)) {
                DefaultRadioButton(
                    text = "System",
                    selected = currentTheme.value == "System",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("System")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Light",
                    selected = currentTheme.value == "Light",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Light")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Dark",
                    selected = currentTheme.value == "Dark",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Dark")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Bumblebee",
                    selected = currentTheme.value == "Bumblebee",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Bumblebee")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Bubblegum",
                    selected = currentTheme.value == "Bubblegum",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Bubblegum")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Github Dark",
                    selected = currentTheme.value == "Github Dark",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Github Dark")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Solarized Dark",
                    selected = currentTheme.value == "Solarized Dark",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Solarized Dark")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Dracula",
                    selected = currentTheme.value == "Dracula",
                    onSelect = {
                        scope.launch {
                            dataStore.saveTheme("Dracula")
                            onDismiss()
                        }
                    })

            }
        }
    )
}