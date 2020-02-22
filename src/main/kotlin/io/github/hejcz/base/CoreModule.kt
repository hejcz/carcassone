package io.github.hejcz.base

import io.github.hejcz.api.*
import io.github.hejcz.base.tile.*
import io.github.hejcz.setup.*

object CoreModule : Extension {

    override fun modify(setup: PiecesSetup) {
        (1..7).forEach { _ -> setup.add(SmallPiece) }
    }

    override fun modify(setup: ValidatorsSetup) {
        setup.add(TilePlacementValidator)
        setup.add(PiecePlacementValidator)
        setup.add(SinglePieceInObjectValidator)
        setup.add(PieceAvailabilityValidator)
    }

    override fun modify(setup: ScoringSetup) {
        setup.add(RewardCompletedCastle(setup.completedCastleScoring()))
        setup.add(RewardIncompleteCastle(setup.incompleteCastleScoring()))
        setup.add(RewardCompletedRoad(setup.completedRoadScoring()))
        setup.add(RewardIncompleteRoad(setup.incompleteRoadScoring()))
        setup.add(RewardCompletedCloister)
        setup.add(RewardIncompleteCloister)
        setup.add(RewardPeasants)
    }

    override fun modify(setup: CommandHandlersSetup) {
        setup.add(BeginGameHandler)
        setup.add(PutTileHandler)
        setup.add(PutPieceHandler)
        setup.add(SkipPieceHandler)
    }

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(
            *listOf(
                2 * TileA,
                4 * TileB,
                1 * TileC,
                4 * TileD,
                4 * TileE,
                1 * TileEWithGarden,
                2 * TileF,
                1 * TileG,
                2 * TileH,
                1 * TileHWithGarden,
                1 * TileI,
                1 * TileIWithGarden,
                3 * TileJ,
                3 * TileK,
                3 * TileL,
                1 * TileM,
                1 * TileMWithGarden,
                2 * TileN,
                1 * TileNWithGarden,
                2 * TileO,
                3 * TileP,
                1 * TileQ,
                2 * TileR,
                1 * TileRWithGarden,
                2 * TileS,
                1 * TileT,
                6 * TileU,
                2 * TileUWithGarden,
                8 * TileV,
                1 * TileVWithGarden,
                4 * TileW,
                1 * TileX
            ).flatten()
            .toTypedArray()
        )
    }

    private operator fun Int.times(tile: BasicTile): List<Tile> = (1..this).map { tile }
}

private object PieceAvailabilityValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> = when (command) {
        is PieceCmd -> isAvailable(state, command.piece)
        else -> emptySet()
    }

    private fun isAvailable(state: State, piece: Piece) =
        if (state.isAvailableForCurrentPlayer(piece)) emptySet() else setOf(NoMeepleEvent(piece))
}

private object PiecePlacementValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PieceCmd -> {
                val role = command.role
                val tile = state.recentTile()
                when {
                    !command.piece.mayBeUsedAs(command.role) -> setOf(
                        InvalidPieceRoleEvent(command.piece, command.role)
                    )
                    role.canBePlacedOn(tile) -> emptySet()
                    else -> setOf(InvalidPieceLocationEvent)
                }
            }
            else -> emptySet()
        }
    }
}

private object SinglePieceInObjectValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> = when (command) {
        is PieceCmd -> when (val role = command.role) {
            is Knight -> {
                val (parts, _) = CastlesExplorer.explore(state, state.recentPosition(), role.direction)
                val pieceAlreadyPresentOnObject = parts
                    .any { part -> state.anyPlayerHasPiece(part.position, Knight(part.direction)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            is Brigand -> {
                val (parts, _) = RoadsExplorer.explore(state, state.recentPosition(), role.direction)
                val pieceAlreadyPresentOnObject =
                    parts.any { part -> state.anyPlayerHasPiece(part.position, Brigand(part.direction)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            is Peasant -> {
                val pieceAlreadyPresentOnObject =
                    GreenFieldsExplorer.explore(state, state.recentPosition(), role.location)
                        .any { (position, location) -> state.anyPlayerHasPiece(position, Peasant(location)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            else -> emptySet()
        }
        else -> emptySet()
    }
}

private object TilePlacementValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> = when (command) {
        is TileCmd -> {
            val isValid = setOf(
                Up, Down,
                Left, Right
            ).all { direction ->
                when (val tile = state.tileAt(direction.move(command.position))) {
                    is NoTile -> true
                    else -> tile.isValidNeighborFor(
                        state.currentTile().rotate(command.rotation),
                        direction.opposite()
                    )
                }
            }
            if (isValid) emptySet() else setOf(InvalidTileLocationEvent)
        }
        else -> emptySet()
    }
}

private object BeginGameHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is BeginCmd
    override fun apply(state: State, command: Command): State = state
}

private object PutPieceHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PieceCmd

    override fun apply(state: State, command: Command): State =
        (command as PieceCmd).let { state.addPiece(command.piece, command.role) }
}

private object PutTileHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is TileCmd

    override fun apply(state: State, command: Command): State =
        (command as TileCmd).let { state.addTile(command.position, command.rotation) }
}

private object SkipPieceHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is SkipPieceCmd
    override fun apply(state: State, command: Command): State = state
}

private class RewardCompletedCastle(private val castleScoring: CastleScoring) : Scoring {

    override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state) + afterPiecePlaced(state, command.role)
        is SkipPieceCmd -> afterTilePlaced(state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        state.getNewCompletedCastles().flatMap {
            val resolvedCastle = it.resolve(state)
            when {
                resolvedCastle.pieces().isNotEmpty() -> generateEvents(resolvedCastle, state)
                else -> emptySet<GameEvent>()
            }
        }

    private fun generateEvents(castle: Castle.Resolved, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(castle.pieces())
        val score = castleScoring(state, castle)
        return winners.ids.map { id -> ScoreEvent(id, score, castle.piecesOf(id)) } +
            losers.ids.map { id -> NoScoreEvent(id, castle.piecesOf(id)) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Knight) {
            return emptySet()
        }
        return state.getNewCompletedCastles()
            .find { it.parts.contains(
                PositionedDirection(state.recentPosition(), role.direction)
            ) }
            // if castle is finished and player is allowed to put piece inside then this is the only one piece on castle
            ?.let { setOf(ScoreEvent(state.currentPlayerId(), castleScoring(state, it), it.resolve(state).piecesOf(state.currentPlayerId()))) }
            ?: emptySet()
    }
}

private class RewardCompletedRoad(private val scoring: RoadScoring) : Scoring {

    override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state) + afterPiecePlaced(state, command.role)
        is SkipPieceCmd -> afterTilePlaced(state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        state.getNewCompletedRoads().flatMap { road ->
            val resolvedRoad = road.resolve(state)
            when {
                resolvedRoad.pieces().isNotEmpty() -> generateEvents(resolvedRoad, state)
                else -> emptySet<GameEvent>()
            }
        }

    private fun generateEvents(road: Road.Resolved, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(road.pieces())
        val score = scoring(state, road)
        return winners.ids.map { id -> road.createPlayerScoredEvent(id, score) } +
            losers.ids.map { id -> road.createOccupiedAreaCompletedEvent(id) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        return state.getNewCompletedRoads()
            .find { it.parts.contains(
                PositionedDirection(state.recentPosition(), role.direction)
            ) }
            // if road is finished and player could put piece then this is the only one piece on this road
            ?.let { setOf(ScoreEvent(state.currentPlayerId(), scoring(state, it), it.resolve(state).piecesOf(state.currentPlayerId()))) }
            ?: emptySet()
    }
}

private object RewardCompletedCloister : Scoring {

    private const val COMPLETED_CLOISTER_REWARD = 9

    override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state.recentPosition(), state) + afterPiecePlaced(state, command.piece, command.role)
        is SkipPieceCmd -> afterTilePlaced(state.recentPosition(), state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .flatMap { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .map { (playerId, pieceOnBoard) -> ScoreEvent(playerId, COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.findPieces(completedCloisterPosition, Monk)

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> =
        when (role) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition()) -> playerScoredEvent(state, piece, role)
                else -> emptySet()
            }
        }

    private fun playerScoredEvent(state: State, piece: Piece, role: Role): Set<ScoreEvent> {
        val pieceOnBoard = PieceOnBoard(state.recentPosition(), piece, role)
        return setOf(ScoreEvent(state.currentPlayerId(), COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)))
    }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}

private class RewardIncompleteCastle(private val scoring: CastleScoring) : EndGameScoring {

    override fun apply(state: State) = state.all(Knight::class)
        .mapNotNull { (_, piece) -> testCastle(state, piece.position, (piece.role as Knight).direction) }
        .distinct()
        .flatMap { castle ->
            when (val score = scoring(state, castle)) {
                0 -> emptyList()
                else -> {
                    val (winners, _) = WinnerSelector.find(castle.resolve(state).pieces())
                    winners.ids.map { id -> ScoreEvent(id, score, emptySet()) }
                }
            }
        }

    private fun testCastle(state: State, startingPosition: Position, startingDirection: Direction): Castle? {
        if (startingDirection !in state.tileAt(startingPosition).exploreCastle(startingDirection)) {
            return null
        }
        val (positionsToDirections, isCompleted) = CastlesExplorer.explore(state, startingPosition, startingDirection)
        return UnresolvedCastle.from(state, positionsToDirections, isCompleted)
    }
}

private class RewardIncompleteRoad(private val scoring: RoadScoring) : EndGameScoring {

    override fun apply(state: State): Collection<GameEvent> {
        return state.all(Brigand::class)
            .mapNotNull { (_, piece) -> explore(state, piece.position, (piece.role as Brigand).direction) }
            .distinct()
            .flatMap { road: Road ->
                val resolvedRoad = road.resolve(state)
                val (winners, _) = WinnerSelector.find(resolvedRoad.pieces())
                when (val score = scoring(state, road)) {
                    0 -> emptyList()
                    else -> winners.ids.map { playerId -> resolvedRoad.createPlayerScoredEventWithoutPieces(playerId, score) }
                }
            }
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road? {
        if (startingDirection !in state.tileAt(startingPosition).exploreRoad(startingDirection)) {
            return null
        }
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return UnresolvedRoad.from(parts, isCompleted)
    }
}

private object RewardIncompleteCloister : EndGameScoring {
    override fun apply(state: State): Collection<GameEvent> =
        state.all(Monk::class).map { (playerId, piece) -> ScoreEvent(playerId, score(state, piece.position), emptySet()) }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}

private object RewardPeasants : EndGameScoring {

    override fun apply(state: State): Collection<GameEvent> {
        return state.all(Peasant::class)
            .map { (playerId, piece) ->
                playerId to GreenFieldsExplorer.explore(
                    state, piece.position, (piece.role as Peasant).location
                )
            }
            .groupBy { (_, fieldParts) -> fieldParts }
            .values
            .flatMap { fieldParts ->
                val completedCastles =
                    fieldParts.first()
                        .second
                        .flatMap { (position, direction) -> reachableCastles(position, direction) }
                        .mapNotNull { state.completedCastle(it) }
                        .distinct()
                        .count()
                if (completedCastles == 0) {
                    return emptySet()
                }
                val countedPieces = fieldParts.groupingBy { (playerId, _) -> playerId }.eachCount()
                val maxElement = countedPieces.maxBy { it.value }!!.value
                countedPieces.filter { it.value == maxElement }
                    .map { ScoreEvent(it.key, 3 * completedCastles, emptySet()) }
            }
    }

    private fun reachableCastles(position: Position, location: Location) =
        setOf(PositionedDirection(position, location.direction)) +
            when (location.side) {
                null -> setOf(
                    PositionedDirection(position, location.direction.left()),
                    PositionedDirection(position, location.direction.right())
                )
                else -> emptySet()
            }
}
