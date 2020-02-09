package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.corncircles.*

class BasicState(players: Collection<Player>, remainingTiles: List<Tile>) :
    State by InnerState(
        players.map { it.id to it }.toMap(),
        drop1IfNotEmpty(remainingTiles),
        Board(mapOf(Position(0, 0) to firstOrNoTile(remainingTiles))),
        players.toList().sortedBy { it.order },
        0,
        firstOrNoTile(drop1IfNotEmpty(remainingTiles)),
        Position(0, 0),
        firstOrNoTile(remainingTiles),
        emptyMap(),
        PiecesOnBoard()
    ) {
    companion object {
        internal fun tileAt(board: Board, position: Position): Tile = board.tiles[position] ?: NoTile
        internal fun firstOrNoTile(tiles: List<Tile>): Tile = tiles.getOrElse(0) { NoTile }
        internal fun drop1IfNotEmpty(tiles: List<Tile>): List<Tile> =
            if (tiles.isEmpty()) tiles else tiles.drop(1)
    }
}

private data class InnerState(
    private val players: Map<Long, Player>,
    private val remainingTiles: List<Tile>,
    private val board: Board,
    private val orderedPlayers: List<Player>,
    private val currentPlayerIndex: Int,
    private val currentTile: Tile,
    private val recentPosition: Position,
    private val recentTile: Tile,
    private val completedCastles: Map<PositionedDirection, CompletedCastle>,
    private val piecesOnBoard: PiecesOnBoard
) : State {

    override fun addTile(position: Position, rotation: Rotation): State {
        val tile = currentTile.rotate(rotation)
        val tilesLeft = BasicState.drop1IfNotEmpty(remainingTiles)
        return copy(
            board = board.withTile(tile, position),
            recentPosition = position,
            recentTile = tile,
            currentTile = BasicState.firstOrNoTile(tilesLeft),
            remainingTiles = tilesLeft
        )
    }

    override fun addPiece(piece: Piece, role: Role): State = doAddPiece(recentPosition, piece, role)

    override fun addPiece(position: Position, piece: Piece, role: Role): State = doAddPiece(position, piece, role)

    private fun doAddPiece(position: Position, piece: Piece, role: Role): InnerState {
        val updatedCurrentPlayer = currentPlayer().lockPiece(piece)
        val updatedPiecesOnBoard = piecesOnBoard.put(updatedCurrentPlayer, position, piece, role)
        return copy(
            piecesOnBoard = updatedPiecesOnBoard,
            players = players + (updatedCurrentPlayer.id to updatedCurrentPlayer),
            orderedPlayers = orderedPlayers.map { if (it.id == updatedCurrentPlayer.id) updatedCurrentPlayer else it }
        )
    }

    override fun removePiece(position: Position, piece: Piece, role: Role): State {
        return doRemovePiece(piece, position, role, currentPlayerId())
    }

    private fun doRemovePiece(piece: Piece, position: Position, role: Role, ownerId: Long): InnerState {
        val updatedOwner = (players[ownerId] ?: error("No player with id $ownerId")).unlockPiece(piece)
        val updatedPiecesOnBoard = piecesOnBoard.remove(updatedOwner, position, piece, role)
        return copy(
            piecesOnBoard = updatedPiecesOnBoard,
            players = players + (updatedOwner.id to updatedOwner),
            orderedPlayers = orderedPlayers.map { if (it.id == updatedOwner.id) updatedOwner else it }
        )
    }

    override fun returnPieces(pieces: Collection<OwnedPiece>): State =
        pieces.fold(this) { acc, (piece, id) -> acc.doRemovePiece(piece.piece, piece.position, piece.role, id) }

    override fun changeActivePlayer(): State = copy(
        currentPlayerIndex = (currentPlayerIndex + 1) % orderedPlayers.size
    )

    override fun addCompletedCastle(completedCastle: CompletedCastle): State = copy(
        completedCastles = completedCastles + completedCastle.elements.associateWith { completedCastle }
    )

    override fun currentTile(): Tile = currentTile

    override fun recentPosition(): Position = recentPosition

    override fun recentTile(): Tile = recentTile

    override fun tileAt(position: Position): Tile = BasicState.tileAt(board, position)

    override fun currentPlayerId(): Long = currentPlayer().id

    override fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    override fun currentTileName() = currentTile.name()

    override fun anyPlayerHasPiece(position: Position, role: Role) = piecesOnBoard.piecesOn(position, role).isNotEmpty()

    override fun allKnights(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allKnights()

    override fun allBrigands(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allBrigands()

    override fun allMonks(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allMonks()

    override fun allAbbots(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allAbbots()

    override fun allPeasants(): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.allPeasants()

    override fun currentPlayerPieces(cornSymbol: CornSymbol): List<Pair<Long, PieceOnBoard>> =
        piecesOnBoard.playerPieces(currentPlayer(), cornSymbol)

    override fun findPieces(position: Position, role: Role): List<Pair<Long, PieceOnBoard>> = piecesOnBoard.piecesOn(position, role)

    override fun allPlayersIdsStartingWithCurrent(): List<Long> {
        val sorted = players.values.sortedBy { it.order }
        return when {
            sorted[0] == currentPlayer() -> sorted.map { it.id }
            else -> sorted.subList(currentPlayer().order - 1, sorted.size).map { it.id } +
                    sorted.subList(0, currentPlayer().order - 1).map { it.id }
        }
    }

    override fun isAvailableForCurrentPlayer(piece: Piece) = currentPlayer().isAvailable(piece)

    override fun previousPlayerId(): Long {
        val order = currentPlayer().order - 1
        val normalizedOrder = if (order == 0) players.count() else order
        return players.values.first { it.order == normalizedOrder }.id
    }

    private fun currentPlayer() = orderedPlayers[currentPlayerIndex]

}
