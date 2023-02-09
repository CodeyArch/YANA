package me.goobydev.composenotes.feature_note.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.goobydev.composenotes.ui.theme.*
import kotlin.math.sqrt

@Entity
data class Note(
    val title: String, // First text field, header for notes
    val content: String, // Second text field, main content of notes
    val timestamp: Long, // Time in millis
    val backgroundColour: Int,
    val textColour: Int,
    var isTrashed: Boolean = false, // Visibility on home/trash pages
    @PrimaryKey val id: Int? = null // Identity in database, used to access and create notes
) {
    companion object {
        // Old colour list, may be deleted from code if no use is found
        val mixedColours = listOf(
            Black,
            VeryDarkGray,
            DarkGray,
            DarkMediumGray,
            MediumGray,
            LightMediumGray,
            LightGray,
            VeryLightGray,
            White,
            Cream,
            LightOrange,
            OrangePink,
            LightPink,
            LightPurple,
            MediumBlue,
            LighterBlue,
            LightestBlue,
            Turquoise,
            LighterGreen,
            RedTerracotta,
            OrangeTerracotta,
            LightOrangeTerracotta,
            PinkOrangeTerracotta,
            YellowTerracotta,
            GreenTerracotta,
            CyanTerracotta,
            TurquoiseTerracotta,
            BlueTerracotta,
            DarkBlueTerracotta,
        )
        // New colours list, has a wider range of colours and supports colours from themes
        val mixedColoursV2 = listOf(
            Black,
            VeryDarkGray,
            GithubDarkGray,
            GithubGray,
            DarkGray,
            DraculaBackground,
            SolarizedDarkGray,
            SolarizedLightGray,
            DarkMediumGray,
            MediumGray,
            LightMediumGray,
            LightGray,
            GithubLightGray,
            VeryLightGray,
            White,
            DraculaForeground,
            Cream,
            SolarizedWhite,
            LightOrange,
            OrangePink,
            BubblegumLightPink,
            LightPink,
            LightPurple,
            BubblegumHotPink,
            DarkBlueTerracotta,
            BlueTerracotta,
            DraculaGray,
            MediumBlue,
            LighterBlue,
            DraculaCyan,
            LightestBlue,
            BubblegumBlue,
            Turquoise,
            LighterGreen,
            GreenTerracotta,
            TurquoiseTerracotta,
            CyanTerracotta,
            BubblegumGreen,
            RedTerracotta,
            OrangeTerracotta,
            GithubOrange,
            PinkOrangeTerracotta,
            LightOrangeTerracotta,
            YellowTerracotta,
            Purple700,
            Purple500,
            Purple200,
            RedOrange,
            RedPink,
            BabyBlue,
            Violet,
            LightGreen,
            Aquamarine,
            Celeste,
            MediumStateBlue,
            Mauve,
            BrightMaroon,
            TurquoiseGreen,
            BubblegumBlueVariant,
            SunriseDarkPurple,
            SunriseMediumDarkPurple,
            SunriseMediumPurple,
            SunriseDarkRed,
            SunriseMediumDarkRed,
            SunriseMediumRed,
            SunriseDarkOrange,
            SunriseMediumDarkOrange,
            SunriseMediumOrange,
            SunriseDarkYellow,
            SunriseMediumDarkYellow,
            SunriseMediumYellow,
            SunriseLightYellow,
            ViridianDarkBlue,
            ViridianMediumBlue,
            ViridianBlue,
            ViridianLightBlue
        ).sortedWith(
            compareBy({
                val hsv = FloatArray(3)
                android.graphics.Color.colorToHSV(it.toArgb(), hsv)
                hsv[0]
            }, {
                val hsv = FloatArray(3)
                android.graphics.Color.colorToHSV(it.toArgb(), hsv)
                hsv[1]
            }, {
                val hsv = FloatArray(3)
                android.graphics.Color.colorToHSV(it.toArgb(), hsv)
                hsv[2]
            })
        )
// This is here to allow for an easy switch to a luminance based sorting system
//            .sortedWith( compareBy(
//                { -getLuminance(it) },
//                { (getHue(it) / 20f ) },
//
//                { getSaturation(it) })
//            )

//
//        Shout out to ChatGPT for teaching me colour theory and helping me with this algorithm
//        private fun getHue(colour: Color): Float {
//            val r = colour.red
//            val g = colour.green
//            val b = colour.blue
//
//            val max = maxOf(r, g, b)
//            val min = minOf(r, g, b)
//            val delta = max - min
//
//            var hue = when (max) {
//                min -> 0f
//                r -> (g - b) / delta % 6
//                g -> (b - r) / delta + 2
//                b -> (r - g) / delta + 4
//                else -> 0f
//            }
//
//            hue *= 60
//            if (hue < 0) hue += 360
//
//            return hue
//        }
//
//        private fun getSaturation(colour: Color): Float {
//            val r = colour.red
//            val g = colour.green
//            val b = colour.blue
//
//            val max = maxOf(r, g, b)
//            val min = minOf(r, g, b)
//            val delta = max - min
//
//            return if (max == 0f) 0f else delta / max
//        }
//
//        private fun getLuminance(colour: Color): Float {
//            val r = colour.red
//            val g = colour.green
//            val b = colour.blue
//
//            return (sqrt(0.2416f * r) + sqrt(0.6918f * g) + sqrt(0.0722f * b))
//        }

    }
}

class InvalidNoteException(message: String): Exception(message)
