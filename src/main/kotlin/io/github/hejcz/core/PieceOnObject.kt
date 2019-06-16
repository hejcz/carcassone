package io.github.hejcz.core

interface PieceOnObject {
    val playerId: Long
    val piece: Piece

    fun power() = piece.power()
}
