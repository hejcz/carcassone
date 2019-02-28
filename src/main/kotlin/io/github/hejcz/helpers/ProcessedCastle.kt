package io.github.hejcz.helpers

data class ProcessedCastle(
    val completed: Boolean,
    val tilesCount: Int,
    val playerId: Long,
    val piecesCount: Int,
    val emblems: Int
)
