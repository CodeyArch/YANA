package me.goobydev.composenotes.feature_note.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import me.goobydev.composenotes.core.presentation.components.NavigationComponent
import me.goobydev.composenotes.ui.theme.ComposeNotesTheme

/* The entry point for the application and where the notes theme is applied. Navigation is created here
and all screens on the app will be listed and referenced here */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposeNotesTheme {
                Surface (
                    color = MaterialTheme.colors.background
                ) {
                    NavigationComponent()
                }
            }
        }
    }
}

