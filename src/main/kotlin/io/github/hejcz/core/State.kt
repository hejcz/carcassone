package io.github.hejcz.core

import io.github.hejcz.core.tile.*

class State(
    val players: Set<Player>,
    private var remainingTiles: RemainingTiles,
    private var board: Board
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

    private val completedCastles = mutableMapOf<PositionedDirection, CompletedCastle>()

    fun addTile(position: Position, rotation: Rotation) {
        val tile = currentTile.rotate(rotation)
        board = board.withTile(tile, position)
        recentPosition = position
        recentTile = tile
        currentTile = remainingTiles.next()
    }

    fun addPiece(piece: Piece, role: Role) =
        currentPlayer.putPiece(recentPosition, piece, role)

    fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    fun currentPlayerId(): Long = currentPlayer.id

    fun endTurn() {
        currentPlayer = queue.next()
    }

    fun addCompletedCastle(completedCastle: CompletedCastle) =
        completedCastle.elements.forEach { completedCastles[it] = completedCastle }

    fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    fun currentTileName() = currentTile.name()

    fun piecesOnPosition(position: Position) = players.flatMap { it.piecesOn(position) }

    fun returnPieces(pieces: Collection<OwnedPiece>): Collection<OwnedPiece> =
        pieces.onEach { piece ->
            players.find { it.id == piece.playerId }!!.returnPieces(setOf(piece.pieceOnBoard))
        }

    fun anyPlayerHasPiece(position: Position, role: Role) = players.any { player -> player.isPieceOn(position, role) }

    inline fun <reified T> all(): List<Pair<Long, PieceOnBoard>> =
        players.flatMap { player -> player.piecesOnBoard().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> piece.role is T }

    fun findPiece(position: Position, role: Role): Pair<Long, PieceOnBoard>? =
        players.mapNotNull { player -> player.pieceOn(position, role)?.let { Pair(player.id, it) } }
            .firstOrNull()

    fun findPieceAsSet(position: Position, role: Role): Set<Pair<Long, PieceOnBoard>> =
        findPiece(position, role)?.let { setOf(it) } ?: emptySet()

    fun castlesDirections(position: Position) = listOf(Up, Right, Down, Left)
        .flatMap { this.tileAt(position).exploreCastle(it) }
        .distinct()

}
