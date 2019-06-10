package io.github.hejcz.helpers

import io.github.hejcz.core.*

interface PieceOnObject {
    val playerId: Long
    val piece: Piece

    fun power() = piece.power()
}
