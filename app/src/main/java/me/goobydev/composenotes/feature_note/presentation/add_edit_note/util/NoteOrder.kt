package me.goobydev.composenotes.feature_note.presentation.add_edit_note.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Colour(orderType: OrderType): NoteOrder(orderType)

    fun copy(orderType: OrderType):NoteOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Colour -> Colour(orderType)
        }
    }
}
