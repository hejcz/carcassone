package io.github.hejcz.rules.helpers

data class ProcessedRoad(
    val completed: Boolean,
    val tilesCount: Int,
    val playerId: Long,
    val piecesCount: Int
)