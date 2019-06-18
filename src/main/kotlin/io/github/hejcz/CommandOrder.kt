package io.github.hejcz

import io.github.hejcz.abbot.*
import io.github.hejcz.core.*
import io.github.hejcz.corncircles.*

class EventsQueue {

    private var expectedCommands: List<Expectation> = listOf(BeginExpectation())

    fun validate(state: State, command: Command): Collection<GameEvent> {
        val expectation = expectedCommands.first()
        return when {
            !expectation.now(command) -> setOf(UnexpectedCommand)
            else -> {
                expectedCommands = expectedCommands.drop(1)
                expectedCommands = expectedCommands + expectation.next(command, state, expectedCommands.isEmpty())
                emptySet()
            }
        }
    }

    fun event(state: State) = expectedCommands.first().toEvent(state)

    fun isPutTileNext(): Boolean = expectedCommands.first() is PutTileExpectation

}

interface Expectation {
    fun now(command: Command): Boolean
    fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation>
    fun toEvent(state: State): GameEvent
}

class BeginExpectation : Expectation {
    override fun now(command: Command) = command is Begin
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = listOf(PutTileExpectation())
    override fun toEvent(state: State): GameEvent = BeginEvent
}

class PutTileExpectation : Expectation {
    override fun now(command: Command): Boolean = command is PutTile

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when (state.currentTile) {
        is CornCircleTile -> listOf(PutPieceExpectation(), ChooseCornCircleActionExpectation())
        else -> listOf(PutPieceExpectation())
    }

    override fun toEvent(state: State): GameEvent = PlaceTile(state.currentTileName(), state.currentPlayerId())
}

class PutPieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is PutPiece || command is PickUpAbbot || command is SkipPiece

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when {
        noExpectations -> listOf(PutTileExpectation())
        else -> emptyList()
    }

    override fun toEvent(state: State): GameEvent = SelectPiece
}

class ChooseCornCircleActionExpectation : Expectation {
    override fun now(command: Command): Boolean = command is ChooseCornCircleActionCommand

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> {
        val cmd = command as ChooseCornCircleActionCommand
        return when (cmd.action) {
            CornCircleAction.ADD_PIECE -> state.allPlayersIdsStartingWithCurrent().map { AddPieceExpectation() } + PutTileExpectation()
            CornCircleAction.REMOVE_PIECE -> state.allPlayersIdsStartingWithCurrent().map { RemovePieceExpectation() } + PutTileExpectation()
        }
    }

    override fun toEvent(state: State): GameEvent = ChooseCornAction(state.currentPlayerId())
}

class AddPieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is AddPieceCommand || command is AvoidCornCircleAction
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = AddPiece(state.currentPlayerId())
}

class RemovePieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is RemovePieceCommand || command is AvoidCornCircleAction
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = RemovePiece(state.currentPlayerId())
}