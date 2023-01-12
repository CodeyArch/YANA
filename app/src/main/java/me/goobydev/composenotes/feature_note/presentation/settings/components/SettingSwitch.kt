package me.goobydev.composenotes.feature_note.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/* A simple composable that changes a boolean to its opposite when clicked. This is used for settings
on the settings screen. */
@Composable
fun SettingSwitch(
    title: String,
    contentDescription: String,
    onClick: () -> Unit,
    checked: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .testTag(
                title
                    .replace(" ", "_")
                    .uppercase()
            )
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.9f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = contentDescription,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSecondary
            )
        }
        Switch(checked = checked, onCheckedChange = null, modifier = Modifier.fillMaxWidth())
        

    }
}