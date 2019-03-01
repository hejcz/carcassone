package io.github.hejcz.helpers

import io.github.hejcz.core.*

data class FoundPiece(
    override val playerId: Long,
    override val piece: Piece,
    val position: Position,
    val direction: Direction
): PieceOnObject
