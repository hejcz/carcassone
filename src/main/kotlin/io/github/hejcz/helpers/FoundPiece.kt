package io.github.hejcz.helpers

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Position

data class FoundPiece(
    val playerId: Long,
    val position: Position,
    val direction: Direction
)
