package io.github.hejcz.base

import io.github.hejcz.api.*
import io.github.hejcz.base.tile.NoTile
import kotlin.reflect.KClass

class InitialState(players: Collection<Player>, remainingTiles: List<Tile>, stateExtensions: Set<StateExtension>) :
    State by CoreState(
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
        piecesOnBoard = PiecesOnBoard(),
        stateExtensions = stateExtensions.associateBy { it.id() }
    ) {
    companion object {
        internal fun firstOrNoTile(tiles: List<Tile>): Tile = tiles.getOrElse(0) { NoTile }
        internal fun drop1IfNotEmpty(tiles: List<Tile>): List<Tile> =
            if (tiles.isEmpty()) tiles else tiles.drop(1)
    }
}

private data class CoreState(
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
    private val piecesOnBoard: PiecesOnBoard,
    private val stateExtensions: Map<StateExtensionId, StateExtension>
) : State {

    override fun addTile(position: Position, rotation: Rotation): State {
        val tile = currentTile.rotate(rotation)
        val tilesLeft = InitialState.drop1IfNotEmpty(remainingTiles)
        val newState = copy(
            board = board.withTile(tile, position),
            recentPosition = position,
            recentTile = tile,
            currentTile = InitialState.firstOrNoTile(tilesLeft),
            remainingTiles = tilesLeft,
            completedCastles = completedCastles.mapValues { it.value.copy(isNew = false) }
        )
        val newCastles = detectClosedCastles(newState).map { CompletedCastle(it, true) }
            .flatMap { castle -> castle.castle.parts.map { it to castle } }
            .toMap()
        val newRoads = detectClosedRoads(newState).map { CompletedRoad(it, true) }
            .flatMap { road -> road.road.parts.map { it to road } }
            .toMap()
        return newState.copy(
            completedCastles = completedCastles + newCastles,
            completedRoads = completedRoads + newRoads
        )
    }

    override fun getNewCompletedCastles(): List<Castle> =
        completedCastles.values.asSequence().filter { it.isNew }.map { it.castle }.distinct().toList()

    override fun getNewCompletedRoads(): List<Road> =
        completedRoads.values.asSequence().filter { it.isNew }.map { it.road.newWith(state = this) }.distinct().toList()

    private fun detectClosedCastles(state: State): List<Castle> =
        castlesDirections(state.tileAt(state.recentPosition()))
            .asSequence()
            .map { exploreCastle(state, state.recentPosition(), it) }
            .distinct()
            .filter { it.completed }
            .filter { it.parts.isNotEmpty() }
            .toList()

    private fun castlesDirections(tile: Tile) = listOf(
        Up, Right,
        Down, Left
    )
        .flatMap { tile.exploreCastle(it) }
        .distinct()

    private fun exploreCastle(state: State, startingPosition: Position, startingDirection: Direction): Castle {
        val (positionsToDirections, isCompleted) = CastlesExplorer.explore(state, startingPosition, startingDirection)
        return UnresolvedCastle.from(state, positionsToDirections, isCompleted)
    }

    private fun detectClosedRoads(state: State): List<Road> =
        listOf(
            Up, Right,
            Down, Left
        )
            .map { exploreRoad(state, state.recentPosition(), it) }
            .filter { it.completed }
            .filter { it.parts.isNotEmpty() }
            .distinct()

    private fun exploreRoad(state: State, startingPosition: Position, startingDirection: Direction): Road {
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return RoadImplementation.from(state, parts, isCompleted)
    }

    override fun addPiece(piece: Piece, role: Role): State = doAddPiece(recentPosition, piece, role)

    override fun addPiece(position: Position, piece: Piece, role: Role): State = doAddPiece(position, piece, role)

    private fun doAddPiece(position: Position, piece: Piece, role: Role): CoreState {
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

    private fun doRemovePiece(piece: Piece, position: Position, role: Role, ownerId: Long): CoreState {
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

    override fun changeActivePlayer(): State = copy(currentPlayerIndex = nextPlayerIndex())

    private fun nextPlayerIndex() = (currentPlayerIndex + 1) % orderedPlayers.size

    override fun update(extension: StateExtension): State =
        copy(stateExtensions = stateExtensions + (extension.id() to extension))

    override fun get(id: StateExtensionId): StateExtension? = stateExtensions[id]

    override fun currentTile(): Tile = currentTile

    override fun recentPosition(): Position = recentPosition

    override fun recentTile(): Tile = recentTile

    override fun tileAt(position: Position): Tile = board.tiles[position] ?: NoTile

    override fun currentPlayerId(): Long = currentPlayer().id

    override fun nextPlayerId(i: Int): Long = orderedPlayers[(currentPlayerIndex + i) % orderedPlayers.size].id

    override fun setCurrentPlayer(currentPlayer: Long): State =
        copy(currentPlayerIndex = orderedPlayers.indexOfFirst { it.id == currentPlayer })

    override fun completedCastle(positionedDirection: PositionedDirection) =
        completedCastles[positionedDirection]?.castle

    override fun currentTileName(): String = currentTile.name()

    override fun anyPlayerHasPiece(position: Position, role: Role) = piecesOnBoard.piecesOn(position, role).isNotEmpty()

    override fun all(clazz: KClass<out Role>): List<OwnedPiece> = piecesOnBoard.get(clazz)

    override fun allOf(clazz: KClass<out Role>, playerId: Long): List<OwnedPiece> =
        all(clazz).filter { it.playerId == playerId }

    override fun findPieces(position: Position, role: Role): List<OwnedPiece> =
        piecesOnBoard.piecesOn(position, role)

    override fun allPlayersIdsStartingWithCurrent(): List<Long> {
        val sorted = players.values.sortedBy { it.order }
        return when {
            sorted[0] == currentPlayer() -> sorted.map { it.id }
            else -> sorted.subList(currentPlayer().order - 1, sorted.size).map { it.id } +
                sorted.subList(0, currentPlayer().order - 1).map { it.id }
        }
    }

    override fun isAvailableForCurrentPlayer(piece: Piece) = currentPlayer().isAvailable(piece)

    override fun findOpenCastles(): Set<PositionedDirection> {
        val allCastles = board.tiles.flatMap { (position, tile) ->
            Direction.ALL.flatMap { tile.exploreCastle(it) }.map {
                PositionedDirection(
                    position, it
                )
            }
        }.toSet()
        return allCastles - completedCastles.keys
    }

    override fun findOpenRoads(): Set<PositionedDirection> {
        val allRoads = board.tiles.flatMap { (position, tile) ->
            Direction.ALL.flatMap { tile.exploreRoad(it) }.map {
                PositionedDirection(
                    position, it
                )
            }
        }.toSet()
        return allRoads - completedRoads.keys
    }

    override fun countPlayers(): Int = players.size

    override fun previousPlayerId(): Long {
        val order = currentPlayer().order - 1
        val normalizedOrder = if (order == 0) players.count() else order
        return players.values.first { it.order == normalizedOrder }.id
    }

    override fun tilesLeft(): Int = remainingTiles.size - 1

    private fun currentPlayer() = orderedPlayers[currentPlayerIndex]

    private data class CompletedCastle(val castle: Castle, val isNew: Boolean)

    private data class CompletedRoad(val road: Road, val isNew: Boolean)
}

private data class Board(val tiles: Map<Position, Tile>) {
    fun withTile(tile: Tile, position: Position) = Board(tiles + (position to tile))
}

private data class PiecesOnBoard(
    private val pieces: Map<KClass<out Role>, List<OwnedPiece>> = emptyMap()
) {
    fun put(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard =
        copy(pieces = pieces + (role::class to
            get(role::class) + OwnedPiece(player.id, PieceOnBoard(position, piece, role))))

    fun remove(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard =
        copy(pieces = pieces + (role::class to
            get(role::class) - OwnedPiece(player.id, PieceOnBoard(position, piece, role))))

    fun piecesOn(position: Position, role: Role): List<OwnedPiece> =
        get(role::class).filter { it.pieceOnBoard.position == position && it.pieceOnBoard.role == role }

    fun get(kClass: KClass<out Role>) = pieces.getOrDefault(kClass, emptyList())
}
