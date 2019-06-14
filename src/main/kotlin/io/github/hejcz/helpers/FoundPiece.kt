package io.github.hejcz.helpers

import io.github.hejcz.core.*

data class FoundPiece(
    override val playerId: Long,
    val pieceOnBoard: PieceOnBoard,
    val position: Position,
    val direction: Direction
) : PieceOnObject {
    override val piece: Piece = pieceOnBoard.piece

    fun toPieceWithOwner() = OwnedPiece(pieceOnBoard, playerId)
}
