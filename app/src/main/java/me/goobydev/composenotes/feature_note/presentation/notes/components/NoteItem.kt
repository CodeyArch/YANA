package me.goobydev.composenotes.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Redo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import me.goobydev.composenotes.R
import me.goobydev.composenotes.feature_note.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*

/* Reusable notes item found on both trash page and notes screen. This item displays data regarding
the note that it is linked to. Clicking the item will navigate the user to the relevant note.
The note item has buttons to delete the item/trash the item and if necessary it has a button to
restore the item, assuming it was trashed. */
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 15.dp,
    cutCornerSize: Dp = 40.dp,
    onDeleteClick: () -> Unit,
    onRestoreClick: () -> Unit,
    isTrashed: Boolean,
    maxLines: Int
) {
    val date = Date(note.timestamp)
    val format = SimpleDateFormat("HH:mm d/MM/yyyy", Locale.getDefault())
    val editDate = format.format(date)
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(note.backgroundColour),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(note.backgroundColour, 0x000000, 0.5f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6,
                color = Color(note.textColour),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (maxLines > 1) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.body1,
                    color = Color(note.textColour),
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(24.dp))
            } else Spacer(modifier = Modifier.height(16.dp))


        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {

            Text(
                text = "Last edit: $editDate",
                style = MaterialTheme.typography.subtitle1,
                color = Color(note.textColour),
                modifier = Modifier.padding(top = 13.dp),
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
            IconButton(
                onClick = onDeleteClick, // Runs the delete note event/trash note event
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_note),
                    tint = Color(note.textColour)
                )
            }
            if(isTrashed) {
                IconButton(
                    onClick = onRestoreClick,
                    /* Runs the delete note event
                    and reinserts the note with the trashed value set to false */
                ) {
                    Icon(
                        imageVector = Icons.Default.Redo,
                        contentDescription = stringResource(R.string.restore_note),
                        tint = Color(note.textColour)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}