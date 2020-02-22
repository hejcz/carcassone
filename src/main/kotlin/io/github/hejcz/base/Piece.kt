package io.github.hejcz.base

import io.github.hejcz.api.Piece
import io.github.hejcz.api.Role

object SmallPiece : Piece {
    override fun power(): Int = 1

    override fun mayBeUsedAs(role: Role): Boolean = role !is Abbot

    override fun toString(): String = "SmallPiece"
}

object BigPiece : Piece {
    override fun power(): Int = 2

    override fun mayBeUsedAs(role: Role): Boolean = role !is Abbot

    override fun toString(): String = "BigPiece"
}

object AbbotPiece : Piece {
    override fun power(): Int = 1

    override fun mayBeUsedAs(role: Role): Boolean = role is Monk || role is Abbot

    override fun toString(): String = "AbbotPiece"
}