package me.goobydev.composenotes.feature_note.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/* A simple composable that has a title text and a description text. This composable is used to open
settings on the settings screen */
@Composable
fun SettingOpen(
    title: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
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
    ) {
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
}