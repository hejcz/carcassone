package io.github.hejcz.core

interface PieceOnObject {
    val piece: Piece
    val isNPC: Boolean
    fun power() = piece.power()
    fun playerId(): Long
}
