package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.corncircles.*

class State(players: Set<Player>, private var remainingTiles: RemainingTiles, private var board: Board) {

    private val players: Map<Long, Player> = players.map { it.id to it }.toMap()

    private val piecesOnBoard = PiecesOnBoard()

    private var queue: PlayersQueue = PlayersQueue(players)

    // player who should make a command
    private var currentPlayer: Player = queue.next()

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
        piecesOnBoard.put(currentPlayer, recentPosition, piece, role)

    fun addPiece(position: Position, piece: Piece, role: Role) =
        piecesOnBoard.put(currentPlayer, position, piece, role)

    fun removePiece(position: Position, piece: Piece, role: Role) =
        piecesOnBoard.remove(currentPlayer, position, piece, role)

    fun returnPieces(pieces: Collection<OwnedPiece>): Collection<OwnedPiece> =
        pieces.onEach {
            val (piece, id) = it
            piecesOnBoard.remove(players.getValue(id), piece.position, piece.piece, piece.role)
        }

    fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    fun currentPlayerId(): Long = currentPlayer.id

    fun changeActivePlayer() {
        currentPlayer = queue.next()
    }

    fun addCompletedCastle(completedCastle: CompletedCastle) =
        completedCastle.elements.forEach { completedCastles[it] = completedCastle }

    fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    fun currentTileName() = currentTile.name()

    fun anyPlayerHasPiece(position: Position, role: Role) = piecesOnBoard.piecesOn(position, role).isNotEmpty()

    fun allKnights(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allKnights()

    fun allBrigands(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allBrigands()

    fun allMonks(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allMonks()

    fun allAbbots(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allAbbots()

    fun allPeasants(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allPeasants()

    fun currentPlayerPieces(cornSymbol: CornSymbol): List<Pair<Long, PieceOnBoard>> =
        piecesOnBoard.playerPieces(currentPlayer, cornSymbol)

    fun findPieces(position: Position, role: Role): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.piecesOn(position, role)

    fun findPieceAsSet(position: Position, role: Role): List<Pair<Long, PieceOnBoard>> = findPieces(position, role)

    fun allPlayersIdsStartingWithCurrent(): List<Long> {
        val sorted = players.values.sortedBy { it.order }
        return when {
            sorted[0] == currentPlayer -> sorted.map { it.id }
            else -> sorted.subList(currentPlayer.order - 1, sorted.size).map { it.id } +
                    sorted.subList(0, currentPlayer.order - 1).map { it.id }
        }
    }

    fun isAvailableForCurrentPlayer(piece: Piece) = currentPlayer.isAvailable(piece)

    fun previousPlayerId(): Long {
        val order = currentPlayer.order - 1
        val normalizedOrder = if (order == 0) players.count() else order
        return players.values.first { it.order == normalizedOrder }.id
    }

}
