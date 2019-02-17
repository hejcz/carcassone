package io.github.hejcz.rules.helpers

data class ProcessedCastle(
    val completed: Boolean,
    val tilesCount: Int,
    val playerId: Long,
    val piecesCount: Int,
    val emblems: Int
)