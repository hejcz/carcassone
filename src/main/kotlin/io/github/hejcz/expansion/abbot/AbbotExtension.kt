package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*
import io.github.hejcz.setup.*
import io.github.hejcz.core.tile.NoTile

object AbbotExtension : Extension {
    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(AbbotPiece)
    override fun modify(commandHandlersSetup: CommandHandlersSetup) = commandHandlersSetup.add(PickUpAbbotHandler)
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.add(GardenCompletedRule, AbbotPickedUp)
        rulesSetup.add(RewardIncompleteGardens)
    }

    override fun modify(validatorsSetup: ValidatorsSetup) = validatorsSetup.add(PickUpAbbotValidator)

    private object AbbotPickedUp : Rule {

        override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
            is PickUpAbbotCmd -> afterAbbotPicked(state, command.position)
            else -> emptySet()
        }

        private fun afterAbbotPicked(state: State, position: Position): Collection<GameEvent> =
            state.findPieces(position, Abbot)
                .find { (playerId, _) -> playerId == state.currentPlayerId() }!!
                .let { (playerId, piece) ->
                    setOf(
                        ScoreEvent(
                            playerId, score(state, piece.position), setOf(PieceOnBoard(position, AbbotPiece, Abbot))
                        )
                    )
                }

        private fun score(state: State, abbotPosition: Position): Int =
            1 + abbotPosition.surrounding().count { state.tileAt(it) !is NoTile }
    }

    private object GardenCompletedRule : Rule {

        override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
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
                    ScoreEvent(state.currentPlayerId(), 9, setOf(PieceOnBoard(state.recentPosition(), AbbotPiece, Abbot)))
                )
            else -> emptySet()
        }
    }

    private object PickUpAbbotHandler : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is PickUpAbbotCmd
        override fun apply(state: State, command: Command): State = state
    }

    private object PickUpAbbotValidator : CommandValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> =
            when {
                command is PickUpAbbotCmd && state.findPieces(command.position, Abbot).isEmpty() -> setOf(CantPickUpAbbotEvent)
                else -> emptySet()
            }
    }

    private object RewardIncompleteGardens : EndRule {
        override fun apply(state: State): Collection<GameEvent> =
            state.all(Abbot::class)
                .map { (playerId, piece) -> ScoreEvent(playerId, score(state, piece.position), emptySet()) }

        private fun score(state: State, cloisterPosition: Position): Int =
            1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
    }
}
