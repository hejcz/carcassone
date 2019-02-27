package io.github.hejcz.core

sealed class Piece {
    abstract fun mayBeUsedAs(pieceRole: PieceRole): Boolean
}

object SmallPiece : Piece() {
    override fun mayBeUsedAs(pieceRole: PieceRole): Boolean = when (pieceRole) {
        is Knight -> true
        is Brigand -> true
        is Peasant -> true
        is Monk -> true
        else -> false
    }
}

object AbbotPiece : Piece() {
    override fun mayBeUsedAs(pieceRole: PieceRole): Boolean = when (pieceRole) {
        is Monk -> true
        is Abbot -> true
        else -> false
    }
}
