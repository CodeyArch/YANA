package me.goobydev.composenotes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.goobydev.composenotes.ui.theme.*

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

        )

    }
}

class InvalidNoteException(message: String): Exception(message)
