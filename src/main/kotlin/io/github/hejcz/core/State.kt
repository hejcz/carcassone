package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.corncircles.*

class State(players: Set<Player>, private var remainingTiles: RemainingTiles, private var board: Board) : IState {

    private val players: Map<Long, Player> = players.map { it.id to it }.toMap()

    private val piecesOnBoard = PiecesOnBoard()

    private var queue: PlayersQueue = PlayersQueue(players)

    // next tile to place on board
    private var currentTile: Tile = remainingTiles.next()

    override fun currentTile(): Tile = currentTile

    // position of recent tile
    private var recentPosition: Position = Position(0, 0)

    override fun recentPosition(): Position = recentPosition

    // recent tile placed on board
    private var recentTile: Tile = tileAt(recentPosition)

    override fun recentTile(): Tile = recentTile

    // collection of completed castles
    private val completedCastles = mutableMapOf<PositionedDirection, CompletedCastle>()

    init {
        queue = queue.next()
    }

    override fun addTile(position: Position, rotation: Rotation): State {
        val tile = currentTile.rotate(rotation)
        board = board.withTile(tile, position)
        recentPosition = position
        recentTile = tile
        currentTile = remainingTiles.next()
        return this
    }

    override fun addPiece(piece: Piece, role: Role): State {
        piecesOnBoard.put(queue.current(), recentPosition, piece, role)
        return this
    }

    override fun addPiece(position: Position, piece: Piece, role: Role): State {
        piecesOnBoard.put(queue.current(), position, piece, role)
        return this
    }

    override fun removePiece(position: Position, piece: Piece, role: Role): State {
        piecesOnBoard.remove(queue.current(), position, piece, role)
        return this
    }

    override fun returnPieces(pieces: Collection<OwnedPiece>): State {
        pieces.onEach {
            val (piece, id) = it
            piecesOnBoard.remove(players.getValue(id), piece.position, piece.piece, piece.role)
        }
        return this
    }

    override fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    override fun currentPlayerId(): Long = queue.current().id

    override fun changeActivePlayer(): State {
        queue.next()
        return this
    }

    override fun addCompletedCastle(completedCastle: CompletedCastle): State {
        completedCastle.elements.forEach { completedCastles[it] = completedCastle }
        return this
    }

    override fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    override fun currentTileName() = currentTile.name()

    override fun anyPlayerHasPiece(position: Position, role: Role) = piecesOnBoard.piecesOn(position, role).isNotEmpty()

    override fun allKnights(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allKnights()

    override fun allBrigands(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allBrigands()

    override fun allMonks(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allMonks()

    override fun allAbbots(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allAbbots()

    override fun allPeasants(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allPeasants()

    override fun currentPlayerPieces(cornSymbol: CornSymbol): List<Pair<Long, PieceOnBoard>> =
        piecesOnBoard.playerPieces(queue.current(), cornSymbol)

    override fun findPieces(position: Position, role: Role): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.piecesOn(position, role)

    override fun allPlayersIdsStartingWithCurrent(): List<Long> {
        val sorted = players.values.sortedBy { it.order }
        return when {
            sorted[0] == queue.current() -> sorted.map { it.id }
            else -> sorted.subList(queue.current().order - 1, sorted.size).map { it.id } +
                    sorted.subList(0, queue.current().order - 1).map { it.id }
        }
    }

    override fun isAvailableForCurrentPlayer(piece: Piece) = queue.current().isAvailable(piece)

    override fun previousPlayerId(): Long {
        val order = queue.current().order - 1
        val normalizedOrder = if (order == 0) players.count() else order
        return players.values.first { it.order == normalizedOrder }.id
    }

}
