package me.goobydev.composenotes.feature_misc_screens.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/* Reusable dropdown component currently used on the help screen to provide info about the
app and its functions */
@Composable
fun InfoDropdown(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RectangleShape
                )
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.h6
                )
                Icon(
                    if( !expanded) Icons.Outlined.ArrowDropDown else Icons.Outlined.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp))
            }
            Column {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp, horizontal = 14.dp),
                        content = content
                    )
                }
            }

        }
    }
}