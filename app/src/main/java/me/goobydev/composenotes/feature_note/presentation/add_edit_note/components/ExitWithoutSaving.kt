package me.goobydev.composenotes.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.goobydev.composenotes.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/* Simplistic alert dialog popup composable that shows when users attempt to exit
a note without saving */
@Composable
fun ExitWithoutSaving(
    onDismiss: () -> Unit,
    onPositiveAction: () -> Unit
) {
    AlertDialog(
        shape = RoundedCornerShape(40.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(R.string.exit_without_saving))
        },
        onDismissRequest = onDismiss,
        text = {
            Text(text = stringResource(R.string.exit_without_save_confirm))
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.no))
                }
                Button(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPositiveAction
                ) {
                    Text(stringResource(R.string.yes))
                }
            }
        }
    )
}