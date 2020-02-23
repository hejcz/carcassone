package io.github.hejcz.base

import io.github.hejcz.api.*

data class FoundPiece(
    val pieceOnBoard: PieceOnBoard,
    val position: Position,
    val direction: Direction,
    private val playerId: Long
) : PieceOnObject {
    override val piece: Piece = pieceOnBoard.piece
    override fun playerId(): Long = playerId
}

interface PieceOnObject {
    val piece: Piece
    fun power() = piece.power()
    fun playerId(): Long
}
