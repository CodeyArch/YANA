package me.goobydev.composenotes.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.goobydev.composenotes.R

/* A collection of font families for the app and their implementation */
val arimo = FontFamily(
    Font(R.font.arimo_regular, FontWeight.Normal),
    Font(R.font.arimo_medium, FontWeight.Medium),
    Font(R.font.arimo_bold, FontWeight.Bold)
)

val roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_mediumitalic, FontWeight.Medium)
)

val reemKufi = FontFamily(
    Font(R.font.reem_kufi_regular, FontWeight.Normal),
    Font(R.font.reem_kufi_medium, FontWeight.Medium),
    Font(R.font.reem_kufi_bold, FontWeight.Bold)
)

val comicSans = FontFamily(
    Font(R.font.qd_better_comic_sans_regular, FontWeight.Normal),
    Font(R.font.qd_better_comic_sans_medium, FontWeight.Medium),
    Font(R.font.qd_better_comic_sans_bold, FontWeight.Bold)
)

val terminus = FontFamily(
    Font(R.font.terminus_regular, FontWeight.Normal),
    Font(R.font.terminus_bold, FontWeight.Medium),
    Font(R.font.terminus_bold, FontWeight.Bold)
)

val jbMono = FontFamily(
    Font(R.font.jetbrainsmono_regular, FontWeight.Normal),
    Font(R.font.jetbrainsmono_medium, FontWeight.Medium),
    Font(R.font.jetbrainsmono_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val DefaultType = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Arimo = Typography(
    defaultFontFamily = arimo
)
val Roboto = Typography(
    defaultFontFamily = roboto
)
val ReemKufi = Typography(
    defaultFontFamily = reemKufi
)
val ComicSans = Typography(
    defaultFontFamily = comicSans
)
val Terminus = Typography(
    defaultFontFamily = terminus
)
val JbMono = Typography(
    defaultFontFamily = jbMono
)