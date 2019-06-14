package io.github.hejcz.core

import io.github.hejcz.basic.tile.*
import io.github.hejcz.helpers.*

class State(
    var board: Board,
    var players: Collection<Player>,
    var remainingTiles: RemainingTiles
) {
    private var queue = PlayersQueue(players)

    var currentPlayer = queue.next()
        private set

    var currentTile: Tile = remainingTiles.next()
        private set

    var recentPosition: Position = Position(0, 0)
        private set

    var recentTile: Tile = tileAt(recentPosition)
        private set

    private val completedCastles = mutableSetOf<CompletedCastle>()

    fun addTile(position: Position, rotation: Rotation) {
        val tile = currentTile.rotate(rotation)
        board = board.withTile(tile, position)
        recentPosition = position
        recentTile = tile
        currentTile = remainingTiles.next()
    }

    fun addPiece(piece: Piece, role: Role) {
        currentPlayer.putPiece(recentPosition, piece, role)
    }

    fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    fun currentPlayerId(): Long = currentPlayer.id

    fun endTurn() {
        currentPlayer = queue.next()
    }

    fun addCompletedCastle(completedCastle: CompletedCastle) {
        completedCastles.add(completedCastle)
    }

    fun completedCastle(positionedDirection: PositionedDirection) =
        completedCastles.find { castle -> castle.elements.any { positionedDirection == it } }

    fun currentTileName() = currentTile.name()

    fun piecesOnPosition(position: Position) = players.flatMap { it.piecesOn(position) }

    fun returnPieces(pieces: Collection<OwnedPiece>): Collection<OwnedPiece> {
        pieces.forEach { piece ->
            players.find { it.id == piece.playerId }!!.returnPieces(setOf(piece.pieceOnBoard))
        }
        return pieces
    }
}
