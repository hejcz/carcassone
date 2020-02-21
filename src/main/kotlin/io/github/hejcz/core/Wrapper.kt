package io.github.hejcz.core

data class FoundPiece(
    val pieceOnBoard: PieceOnBoard,
    val position: Position,
    val direction: Direction,
    private val playerId: Long
) : PieceOnObject {
    override val piece: Piece = pieceOnBoard.piece
    override fun playerId(): Long = playerId
}

data class GameChanges(val state: State, val events: Collection<GameEvent>)

interface PieceOnObject {
    val piece: Piece
    fun power() = piece.power()
    fun playerId(): Long
}

data class PositionedDirection(val position: Position, val direction: Direction)

data class PieceOnBoard(val position: Position, val piece: Piece, val role: Role)

data class OwnedPiece(val playerId: Long, val pieceOnBoard: PieceOnBoard)
