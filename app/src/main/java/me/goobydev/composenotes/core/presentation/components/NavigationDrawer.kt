package me.goobydev.composenotes.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import me.goobydev.composenotes.R
import me.goobydev.composenotes.feature_note.presentation.util.Screen
/* Data class for the construction of a navigation drawer item */
data class NavigationDrawerItem(
    val icon: Painter, // May migrate this to Icon rather than Painter
    val label: String,
    val route: String
)

/* Navigation drawer composable used on most screens to allow access to other screens */
@Composable
fun NavDrawer(
    navController: NavController
) {
    val itemsList = navigationDrawerItemsList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 36.dp)
    ) {
        items(itemsList) { item ->
            NavigationListItem(
                item = item,
                itemClick = {
                    navController.navigate(route = item.route)
                }
            )
        }
    }
}
/* List of navigation items, to access new screens you may wish to add them here */
@Composable
fun navigationDrawerItemsList(): List<NavigationDrawerItem> {
    val itemsList = arrayListOf<NavigationDrawerItem>()

    itemsList.add(
        NavigationDrawerItem(
            icon = painterResource(id = R.drawable.home),
            label = "Home",
            route = Screen.NotesScreen.route
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            icon = painterResource(id = R.drawable.settings),
            label = "Settings",
            route = Screen.SettingsScreen.route
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            icon = painterResource(id = R.drawable.archive),
            label = "Archive",
            route = Screen.TrashedNotesScreen.route
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            icon = painterResource(id = R.drawable.help_circle),
            label = "Help",
            route = Screen.HelpScreen.route
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            icon = painterResource(id = R.drawable.info),
            label = "About",
            route = Screen.AboutScreen.route
        )
    )

    return itemsList
}

/* Nav list item composable, simple row with icon and text that when clicked will navigate to the desired screen */
@Composable
fun NavigationListItem(
    item: NavigationDrawerItem,
    itemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itemClick()
            }
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .testTag(item.label.uppercase()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(all = 2.dp)
                .size(size = 28.dp),
            painter = item.icon,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
