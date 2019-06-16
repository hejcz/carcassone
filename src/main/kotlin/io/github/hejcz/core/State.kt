package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import kotlin.reflect.*

class PiecesOnBoard {
    val knights: List<PieceOnBoard> = mutableListOf()
    val brigands: List<PieceOnBoard> = mutableListOf()
    val abbots: List<PieceOnBoard> = mutableListOf()
    val monks: List<PieceOnBoard> = mutableListOf()
    val peasants: List<PieceOnBoard> = mutableListOf()
}

class State(
    private val players: Set<Player>,
    private var remainingTiles: RemainingTiles,
    private var board: Board
) {
    private var queue: PlayersQueue = PlayersQueue(players)

    // player who should make a command
    var currentPlayer: Player = queue.next()
        private set

    // next tile to place on board
    var currentTile: Tile = remainingTiles.next()
        private set

    // position of recent tile
    var recentPosition: Position = Position(0, 0)
        private set

    // recent tile placed on board
    var recentTile: Tile = tileAt(recentPosition)
        private set

    // collection of completed castles
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

    fun addPiece(position: Position, piece: Piece, role: Role) =
        currentPlayer.putPiece(position, piece, role)

    fun removePiece(position: Position, piece: Piece, role: Role) =
        currentPlayer.removePiece(position, piece, role)

    fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    fun currentPlayerId(): Long = currentPlayer.id

    fun changeActivePlayer() {
        currentPlayer = queue.next()
    }

    fun addCompletedCastle(completedCastle: CompletedCastle) =
        completedCastle.elements.forEach { completedCastles[it] = completedCastle }

    fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    fun currentTileName() = currentTile.name()

    fun returnPieces(pieces: Collection<OwnedPiece>): Collection<OwnedPiece> =
        pieces.onEach { piece ->
            players.find { it.id == piece.playerId }!!.returnPieces(setOf(piece.pieceOnBoard))
        }

    fun anyPlayerHasPiece(position: Position, role: Role) = players.any { player -> player.isPieceOn(position, role) }

    fun <T : Role> all(clazz: KClass<T>): List<Pair<Long, PieceOnBoard>> =
        players.flatMap { player -> player.piecesOnBoard().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> clazz.isInstance(piece.role) }

    fun findPiece(position: Position, role: Role): Pair<Long, PieceOnBoard>? =
        players.mapNotNull { player -> player.pieceOn(position, role)?.let { Pair(player.id, it) } }
            .firstOrNull()

    fun findPieceAsSet(position: Position, role: Role): Set<Pair<Long, PieceOnBoard>> =
        findPiece(position, role)?.let { setOf(it) } ?: emptySet()

    fun castlesDirections(position: Position) = listOf(Up, Right, Down, Left)
        .flatMap { this.tileAt(position).exploreCastle(it) }
        .distinct()

    fun allPlayersIdsStartingWithCurrent(): List<Long> {
        val sorted = players.sortedBy { it.order }
        return when {
            sorted[0] == currentPlayer -> sorted.map { it.id }
            else -> sorted.subList(currentPlayer.order - 1, sorted.size).map { it.id } +
                    sorted.subList(0, currentPlayer.order - 1).map { it.id }
        }
    }

}
