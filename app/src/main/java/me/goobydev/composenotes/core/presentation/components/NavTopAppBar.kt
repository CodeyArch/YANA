package me.goobydev.composenotes.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/* Top App Bar Composable used on the majority of screens throughout the notes app,
opens the navigation drawer */
@Composable
fun NavTopAppBar(onNavIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Yet Another Notes App") },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavIconClick()
                },
                modifier = Modifier.testTag("NAV_DRAWER_BUTTON")
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
        }
    )
}