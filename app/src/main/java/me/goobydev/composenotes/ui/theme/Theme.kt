package me.goobydev.composenotes.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.goobydev.composenotes.feature_settings.data.SaveUserFont
import me.goobydev.composenotes.feature_settings.data.SaveUserTheme

/* A collection of the themes for the app and the implementation of applying them */
private val DarkColorPalette = darkColors(
    primary = Black,
    primaryVariant = VeryDarkGray,
    onPrimary = White,
    secondary = Teal200,
    onSecondary = DarkMediumGray,
)

private val LightColorPalette = lightColors(
    primary = White,
    primaryVariant = Purple700,
    onPrimary = Black,
    secondary = Teal200,
    onSecondary = DarkMediumGray,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@SuppressLint("ConflictingOnColor")
private val YellowPalette = lightColors(
    primary = LightYellow,
    surface = YellowTerracotta,
    primaryVariant = OrangeTerracotta,
    secondary = BubblegumYellow,
    secondaryVariant = BubblegumYellow,
    background = YellowTerracotta,
    onPrimary = Black,
    onSecondary = DarkMediumGray,
    onBackground = Black,
    onSurface = Black
)

@SuppressLint("ConflictingOnColor")
private val Bubblegum = lightColors(
    primary = BubblegumBlue,
    surface = BubblegumLightPink,
    primaryVariant = BubblegumHotPink,
    secondary = BubblegumHotPink,
    secondaryVariant = BubblegumLightPink,
    background = BubblegumBlue,
    onPrimary = Black,
    onSecondary = DarkGray,
    onBackground = Black,
    onSurface = Black
)

@SuppressLint("ConflictingOnColor")
private val GithubDark = darkColors(
    primary = GithubOrange,
    surface = GithubDarkGray,
    primaryVariant = GithubDarkGray,
    onPrimary = GithubLightGray,
    secondary = GithubLightGray,
    onSecondary = VeryLightGray,
    background = GithubGray,
    onBackground = Cream,
    onSurface = GithubLightGray
)

private val SolarizedDark = darkColors(
    primary = SolarizedBlack,
    surface = SolarizedBlack,
    primaryVariant = SolarizedBlack,
    onPrimary = SolarizedWhite,
    secondary = Teal200,
    onSecondary = DarkMediumGray,
    background = SolarizedBlack,
    onBackground = SolarizedWhite,
    onSurface = SolarizedWhite
)

private val Dracula = darkColors(
    primary = DraculaBackground,
    surface = DraculaBackground,
    primaryVariant = DraculaBackground,
    onPrimary = DraculaForeground,
    secondary = DraculaCyan,
    onSecondary = DraculaGray,
    background = DraculaBackground,
    onBackground = DraculaForeground,
    onSurface = DraculaForeground
)

@Composable
fun ComposeNotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val context = LocalContext.current
    val themeDataStore = SaveUserTheme(context)
    val currentTheme = themeDataStore.getTheme.collectAsState(initial = "")
    val fontDataStore = SaveUserFont(context)
    val currentFont = fontDataStore.getFont.collectAsState(initial = "")

    val colors = when(currentTheme.value) {
        "System" -> if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
        "Dark" -> DarkColorPalette
        "Light" -> LightColorPalette
        "Bumblebee" -> YellowPalette
        "Bubblegum" -> Bubblegum
        "Github Dark" -> GithubDark
        "Solarized Dark" -> SolarizedDark
        "Dracula" -> Dracula
        else -> if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = when(currentTheme.value) {
                "System" -> if (darkTheme) {
                    Color.Black
                } else {
                    Color.White
                }
                "Dark" -> Color.Black
                "Light" -> Color.White
                "Bumblebee" -> LightYellow
                "Bubblegum" -> BubblegumLightPink
                "Github Dark" -> GithubOrange
                "Solarized Dark" -> SolarizedBlack
                "Dracula" -> DraculaBackground
                else -> if (darkTheme) {
                    Color.Black
                } else {
                    Color.White
                }
            }
        )
    }
    val typography = when(currentFont.value) {
        "System" -> DefaultType
        "Arimo" -> Arimo
        "Roboto" -> Roboto
        "Reem Kufi" -> ReemKufi
        "Comic Sans" -> ComicSans
        "Terminus" -> Terminus
        "Jetbrains Mono" -> JbMono
        else -> DefaultType
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}