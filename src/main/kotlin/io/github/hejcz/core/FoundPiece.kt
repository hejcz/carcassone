package io.github.hejcz.core

data class FoundPiece(
    val pieceOnBoard: PieceOnBoard,
    val position: Position,
    val direction: Direction,
    private val playerId: Long? = null,
    override val isNPC: Boolean = false
) : PieceOnObject {
    override val piece: Piece = pieceOnBoard.piece
    override fun playerId(): Long = playerId!!
}
