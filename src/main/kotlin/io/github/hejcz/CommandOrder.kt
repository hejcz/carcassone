package io.github.hejcz

import io.github.hejcz.abbot.*
import io.github.hejcz.core.*
import io.github.hejcz.corncircles.*

class ExpectedCommandValidator : CommandValidator {

    private var expectedCommands: List<Expectation> = listOf(BeginExpectation())

    override fun validate(state: State, command: Command): Collection<GameEvent> {
        val expectation = expectedCommands.first()
        return when {
            !expectation.now(command) -> setOf(InvalidCommand)
            else -> {
                expectedCommands = expectedCommands.drop(1)
                expectedCommands = expectedCommands + expectation.next(command, state, expectedCommands.isEmpty())
                emptySet()
            }
        }
    }
}

interface Expectation {
    fun now(command: Command): Boolean
    fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation>
}

class BeginExpectation : Expectation {
    override fun now(command: Command) = command is Begin
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = listOf(PutTileExpectation())
}

class PutTileExpectation : Expectation {
    override fun now(command: Command): Boolean = command is PutTile

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when (state.currentTile) {
        is CornCircleTile -> listOf(PutPieceExpectation(), ChooseCornCircleActionExpectation())
        else -> listOf(PutPieceExpectation())
    }
}

class PutPieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is PutPiece || command is PickUpAbbot || command is SkipPiece

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when {
        noExpectations -> listOf(PutTileExpectation())
        else -> emptyList()
    }
}

class ChooseCornCircleActionExpectation : Expectation {
    override fun now(command: Command): Boolean = command is ChooseCornCircleAction

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> {
        val cmd = command as ChooseCornCircleAction
        return when (cmd.action) {
            CornCircleAction.ADD_PIECE -> state.allPlayersIdsStartingWithCurrent().map { AddPieceExpectation() } + PutTileExpectation()
            CornCircleAction.REMOVE_PIECE -> state.allPlayersIdsStartingWithCurrent().map { RemovePieceExpectation() } + PutTileExpectation()
        }
    }
}

class AddPieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is AddPiece || command is SkipPiece
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
}

class RemovePieceExpectation : Expectation {
    override fun now(command: Command): Boolean = command is RemovePiece || command is SkipPiece
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
}