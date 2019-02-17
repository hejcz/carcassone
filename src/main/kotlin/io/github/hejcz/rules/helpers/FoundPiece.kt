package io.github.hejcz.rules.helpers

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Position

data class FoundPiece(
        val playerId: Long,
        val position: Position,
        val direction: Direction
)