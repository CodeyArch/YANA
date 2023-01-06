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
import me.goobydev.composenotes.feature_settings.data.SaveUserFont

/* A composable to allow users to select the global font for the app. It takes and saves
preferences using the SaveUserFont.kt preferences Datastore */
@Composable
fun FontSelect(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = SaveUserFont(context)
    val currentTheme = dataStore.getFont.collectAsState(initial = "")

    AlertDialog(
        shape = RoundedCornerShape(40.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(R.string.fonts))
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
                            dataStore.saveFont("System")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Arimo",
                    selected = currentTheme.value == "Arimo",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Arimo")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Roboto",
                    selected = currentTheme.value == "Roboto",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Roboto")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Reem Kufi",
                    selected = currentTheme.value == "Reem Kufi",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Reem Kufi")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Comic Sans",
                    selected = currentTheme.value == "Comic Sans",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Comic Sans")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Terminus",
                    selected = currentTheme.value == "Terminus",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Terminus")
                            onDismiss()
                        }
                    })
                Spacer(modifier = Modifier.height(4.dp))
                DefaultRadioButton(
                    text = "Jetbrains Mono",
                    selected = currentTheme.value == "Jetbrains Mono",
                    onSelect = {
                        scope.launch {
                            dataStore.saveFont("Jetbrains Mono")
                            onDismiss()
                        }
                    })
            }
        }
    )
}