package io.github.hejcz.components.abbot

import io.github.hejcz.api.*
import io.github.hejcz.base.AbbotPiece
import io.github.hejcz.base.*
import io.github.hejcz.setup.*
import io.github.hejcz.base.tile.NoTile

object AbbotExtension : Extension {
    override fun modify(setup: PiecesSetup) = setup.add(AbbotPiece)
    override fun modify(setup: CommandHandlersSetup) = setup.add(PickUpAbbotHandler)
    override fun modify(setup: ScoringSetup) {
        setup.add(GardenCompletedScoring, AbbotPickedUp)
        setup.add(RewardIncompleteGardens)
    }

    override fun modify(setup: ValidatorsSetup) = setup.add(PickUpAbbotValidator)

    private object AbbotPickedUp : Scoring {

        override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
            is PickUpAbbotCmd -> afterAbbotPicked(state, command.position)
            else -> emptySet()
        }

        private fun afterAbbotPicked(state: State, position: Position): Collection<GameEvent> =
            state.findPieces(position, Abbot)
                .find { (playerId, _) -> playerId == state.currentPlayerId() }!!
                .let { (playerId, piece) ->
                    setOf(
                        ScoreEvent(
                            playerId, score(state, piece.position), setOf(
                                PieceOnBoard(
                                    position,
                                    AbbotPiece, Abbot
                                )
                            )
                        )
                    )
                }

        private fun score(state: State, abbotPosition: Position): Int =
            1 + abbotPosition.surrounding().count { state.tileAt(it) !is NoTile }
    }

    private object GardenCompletedScoring : Scoring {

        override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
            is PieceCmd -> afterTilePlaced(state.recentPosition(), state) + afterPiecePlaced(state, command.role)
            is SkipPieceCmd -> afterTilePlaced(state.recentPosition(), state)
            else -> emptySet()
        }.distinct()

        private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
            position.surrounding()
                .filter { state.tileAt(it).hasGarden() && it.isSurrounded(state) }
                .flatMap { gardenPosition -> pieceWithOwner(state, gardenPosition) }
                .map { (playerId, pieceOnBoard) -> ScoreEvent(playerId, 9, setOf(pieceOnBoard)) }

        private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
            state.findPieces(completedCloisterPosition, Abbot)

        private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> = when {
            role is Abbot && state.recentPosition().isSurrounded(state) ->
                setOf(
                    ScoreEvent(state.currentPlayerId(), 9, setOf(
                        PieceOnBoard(
                            state.recentPosition(),
                            AbbotPiece, Abbot
                        )
                    ))
                )
            else -> emptySet()
        }
    }

    private val PickUpAbbotHandler = cmdHandler<PickUpAbbotCmd>()

    private object PickUpAbbotValidator : CmdValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> =
            when {
                command is PickUpAbbotCmd && state.findPieces(command.position, Abbot).isEmpty() -> setOf(CantPickUpAbbotEvent)
                else -> emptySet()
            }
    }

    private object RewardIncompleteGardens : EndGameScoring {
        override fun apply(state: State): Collection<GameEvent> =
            state.all(Abbot::class)
                .map { (playerId, piece) -> ScoreEvent(playerId, score(state, piece.position), emptySet()) }

        private fun score(state: State, cloisterPosition: Position): Int =
            1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
    }
}
