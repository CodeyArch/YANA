package me.goobydev.composenotes.feature_misc_screens.presentation.about

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.goobydev.composenotes.R
import me.goobydev.composenotes.core.presentation.components.NavDrawer
import me.goobydev.composenotes.core.presentation.components.NavTopAppBar

/* About screen for the notes app, tells the user about the creation of the app and
provides them links to resources to learn more, including Discord and Github.
Eventually a Patreon link may be provided on this page too */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavController) {
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
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text("""
                    Yet Another Note App
                    
                    This app started as a learning project for a new developer and is primarily developed and maintained by said developer. If you would like to contact the developer, support them, or just see how this app was developed, use the buttons below
                """.trimIndent())
            }
            val uriHandler = LocalUriHandler.current
            val isLightTheme = MaterialTheme.colors.isLight
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.15f)
                        .padding(4.dp),
                    onClick = { uriHandler.openUri("https://discord.gg/n4gKz8J8W2") }
                ) {
                    Image(
                        painter = painterResource(id = if (isLightTheme) {
                            R.drawable.full_logo_black_rgb
                        } else {
                            R.drawable.full_logo_blurple_rgb
                        }),
                        contentDescription = "Click to join discord" )

                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .fillMaxHeight(0.15f)
                        .padding(4.dp),
                    onClick = { uriHandler.openUri("https://github.com/CodeyArch/YANA") }) {
                    Image(
                        painter = painterResource(id = if (isLightTheme) {
                            R.drawable.github_logo
                        } else {
                            R.drawable.github_logo_white
                        }),
                        contentDescription = "Click to open Github" )

                }
            }

        }
    }
}