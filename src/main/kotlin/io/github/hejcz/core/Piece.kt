package io.github.hejcz.core

sealed class Piece {
    abstract fun mayBeUsedAs(role: Role): Boolean
    abstract fun power(): Int
}

object SmallPiece : Piece() {
    override fun power(): Int = 1

    override fun mayBeUsedAs(role: Role): Boolean = role !is Abbot
}

object BigPiece : Piece() {
    override fun power(): Int = 2

    override fun mayBeUsedAs(role: Role): Boolean = role !is Abbot
}

object AbbotPiece : Piece() {
    override fun power(): Int = 1

    override fun mayBeUsedAs(role: Role): Boolean = role is Monk || role is Abbot
}
