package me.goobydev.composenotes.feature_note.presentation.add_edit_note.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
