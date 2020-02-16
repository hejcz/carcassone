package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.corncircles.*

class BasicState(players: Collection<Player>, remainingTiles: List<Tile>) :
    State by InnerState(
        board = Board(mapOf(Position(0, 0) to firstOrNoTile(remainingTiles))),
        players = players.associateBy { it.id },
        orderedPlayers = players.sortedBy { it.order },
        currentPlayerIndex = 0,
        remainingTiles = drop1IfNotEmpty(remainingTiles),
        currentTile = firstOrNoTile(drop1IfNotEmpty(remainingTiles)),
        recentTile = firstOrNoTile(remainingTiles),
        recentPosition = Position(0, 0),
        completedCastles = emptyMap(),
        completedRoads = emptyMap(),
        piecesOnBoard = PiecesOnBoard()
    ) {
    companion object {
        internal fun tileAt(board: Board, position: Position): Tile = board.tiles[position] ?: NoTile
        internal fun firstOrNoTile(tiles: List<Tile>): Tile = tiles.getOrElse(0) { NoTile }
        internal fun drop1IfNotEmpty(tiles: List<Tile>): List<Tile> =
            if (tiles.isEmpty()) tiles else tiles.drop(1)
    }
}

private data class InnerState(
    private val board: Board,
    private val players: Map<Long, Player>,
    private val orderedPlayers: List<Player>,
    private val currentPlayerIndex: Int,
    private val remainingTiles: List<Tile>,
    private val currentTile: Tile,
    private val recentTile: Tile,
    private val recentPosition: Position,
    private val completedCastles: Map<PositionedDirection, CompletedCastle>,
    private val completedRoads: Map<PositionedDirection, CompletedRoad>,
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

    override fun addNpcPiece(piece: NpcPiece, position: Position, direction: Direction): State =
        copy(piecesOnBoard = piecesOnBoard.putNPC(piece, position, direction))

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
        pieces.fold(this) { acc, (id, piece) -> acc.doRemovePiece(piece.piece, piece.position, piece.role, id) }

    override fun changeActivePlayer(): State = copy(
        currentPlayerIndex = (currentPlayerIndex + 1) % orderedPlayers.size
    )

    override fun addCompletedCastle(completedCastle: CompletedCastle): State = copy(
        completedCastles = completedCastles + completedCastle.elements.associateWith { completedCastle }
    )

    override fun addCompletedRoad(completedRoad: CompletedRoad): State = copy(
        completedRoads = completedRoads + completedRoad.elements.associateWith { completedRoad }
    )

    override fun currentTile(): Tile = currentTile

    override fun recentPosition(): Position = recentPosition

    override fun recentTile(): Tile = recentTile

    override fun tileAt(position: Position): Tile = BasicState.tileAt(board, position)

    override fun currentPlayerId(): Long = currentPlayer().id

    override fun completedCastle(positionedDirection: PositionedDirection) = completedCastles[positionedDirection]

    override fun currentTileName() = currentTile.name()

    override fun anyPlayerHasPiece(position: Position, role: Role) = piecesOnBoard.piecesOn(position, role).isNotEmpty()

    override fun allKnights(): List<OwnedPiece> = piecesOnBoard.allKnights()

    override fun allBrigands(): List<OwnedPiece> = piecesOnBoard.allBrigands()

    override fun allMonks(): List<OwnedPiece> = piecesOnBoard.allMonks()

    override fun allAbbots(): List<OwnedPiece> = piecesOnBoard.allAbbots()

    override fun allPeasants(): List<OwnedPiece> = piecesOnBoard.allPeasants()

    override fun currentPlayerPieces(cornSymbol: CornSymbol): List<OwnedPiece> =
        piecesOnBoard.playerPieces(currentPlayer(), cornSymbol)

    override fun findPieces(position: Position, role: Role): List<OwnedPiece> =
        piecesOnBoard.piecesOn(position, role)

    override fun exists(position: Position, direction: Direction, piece: NpcPiece): Boolean =
        piecesOnBoard.containsNPC(position, direction, piece)

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

    override fun mageOrWitchMustBeInstantlyMoved(): Boolean {
        val magician = piecesOnBoard.magician()
        val witch = piecesOnBoard.witch()
        if (magician == null || witch == null) {
            return false
        }
        val isMagicianOnCastle = tileAt(magician.position).exploreCastle(magician.direction).isNotEmpty()
        val isWitchOnCastle = tileAt(witch.position).exploreCastle(witch.direction).isNotEmpty()
        return when {
            isMagicianOnCastle != isWitchOnCastle -> false
            isMagicianOnCastle -> CastlesExplorer.explore(this, magician.position, magician.direction).first.contains(
                PositionedDirection(witch.position, witch.direction))
            else -> RoadsExplorer.explore(this, magician.position, magician.direction).first.contains(
                PositionedDirection(witch.position, witch.direction))
        }
    }

    override fun canBePickedUp(piece: NpcPiece): Boolean {
        val (actual, other) = when (piece) {
            MagePiece -> piecesOnBoard.magician() to piecesOnBoard.witch()
            WitchPiece -> piecesOnBoard.witch() to piecesOnBoard.magician()
        }
        if (actual == null) {
            return false
        }
        val openPositions = findOpenCastles() + findOpenRoads()
        val actualPositions = exploreRoadOrCastle(actual)
        val otherPositions = other?.let { exploreRoadOrCastle(it) } ?: emptySet()
        val allowedPositions = openPositions - actualPositions - otherPositions
        return allowedPositions.isEmpty()
    }

    override fun canBePlacedOn(piece: NpcPiece, targetPos: PositionedDirection): Boolean {
        val (actual, other) = when (piece) {
            MagePiece -> piecesOnBoard.magician() to piecesOnBoard.witch()
            WitchPiece -> piecesOnBoard.witch() to piecesOnBoard.magician()
        }
        val openCastles = findOpenCastles()
        val openRoads = findOpenRoads()
        val isOpenObject = openCastles.contains(targetPos) || openRoads.contains(targetPos)
        if (actual == null && other == null) {
            return isOpenObject
        }
        if (actual != null && exploreRoadOrCastle(actual).contains(targetPos)) {
            return false
        }
        if (other != null && exploreRoadOrCastle(other).contains(targetPos)) {
            return false
        }
        return isOpenObject
    }

    private fun exploreRoadOrCastle(npc: NPCOnBoard): Set<PositionedDirection> {
        return when {
            tileAt(npc.position).exploreCastle(npc.direction).isNotEmpty() ->
                CastlesExplorer.explore(this, npc.position, npc.direction).first
            else ->
                RoadsExplorer.explore(this, npc.position, npc.direction).first
        }
    }

    private fun findOpenCastles(): Set<PositionedDirection> {
        val allCastles = board.tiles.flatMap { (position, tile) ->
            Direction.ALL.flatMap { tile.exploreCastle(it) }.map { PositionedDirection(position, it) }
        }.toSet()
        return allCastles - completedCastles.keys
    }

    private fun findOpenRoads(): Set<PositionedDirection> {
        val allRoads = board.tiles.flatMap { (position, tile) ->
            Direction.ALL.flatMap { tile.exploreRoad(it) }.map { PositionedDirection(position, it) }
        }.toSet()
        return allRoads - completedRoads.keys
    }

    private fun currentPlayer() = orderedPlayers[currentPlayerIndex]
}
