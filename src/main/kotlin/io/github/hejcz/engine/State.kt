package io.github.hejcz.engine

import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.Position
import io.github.hejcz.placement.PositionedDirection
import io.github.hejcz.placement.Rotation
import io.github.hejcz.tiles.basic.NoTile
import io.github.hejcz.tiles.basic.Tile

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

    fun addTile(position: Position, rotation: Rotation) {
        val tile = currentTile.rotate(rotation)
        board = board.withTile(tile, position)
        recentPosition = position
        recentTile = tile
        currentTile = remainingTiles.next()
    }

    fun addPiece(pieceId: PieceId, pieceRole: PieceRole) {
        currentPlayer.putPiece(recentPosition, pieceId, pieceRole)
    }

    fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile
    fun currentPlayerId(): Long = currentPlayer.id
    fun endTurn() {
        currentPlayer = queue.next()
    }

    val completedCastles = mutableSetOf<CompletedCastle>()

    fun addCompletedCastle(completedCastle: CompletedCastle) {
        completedCastles.add(completedCastle)
    }

    fun completedCastle(positionedDirection: PositionedDirection) =
        completedCastles.find { castle -> castle.elements.any { positionedDirection == it } }


}

