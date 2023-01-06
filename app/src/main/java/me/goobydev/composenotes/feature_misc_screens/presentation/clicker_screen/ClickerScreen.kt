package me.goobydev.composenotes.feature_misc_screens.presentation.clicker_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar

/* This screen is unfinished. The purpose of this screen is for a fun but pointless clicker
game in which users would click a button and a number would go up. They would be given a title
at the top of the screen relative to their current score. The score would persist using DataStore */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClickerScreen(navController: NavController) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "This is the Easter Egg Clicker screen")
        }
    }
}