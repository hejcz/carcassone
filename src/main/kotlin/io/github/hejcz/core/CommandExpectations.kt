package io.github.hejcz.core

import io.github.hejcz.abbot.*
import io.github.hejcz.corncircles.*
import io.github.hejcz.magic.MagicTile
import io.github.hejcz.magic.MoveMagicianOrWitchCommand
import io.github.hejcz.magic.PickUpMagicianOrWitch
import io.github.hejcz.magic.PlaceWitchOrMagician

interface Expectation {
    fun expects(command: Command): Boolean
    fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation>
    fun toEvent(state: State): GameEvent
    fun shouldRunRules(state: State): Boolean = true
}

class BeginExpectation : Expectation {
    override fun expects(command: Command) = command is Begin
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> =
        listOf(PutTileExpectation())

    override fun toEvent(state: State): GameEvent = BeginEvent
}

class PutTileExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is PutTile

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> =
        when {
            state.recentTile() is CornCircleTile -> listOf(PutPieceExpectation(), ChooseCornCircleActionExpectation())
            state.recentTile() is MagicTile || state.mageOrWitchMustBeInstantlyMoved() ->
                listOf(MoveMagicianOrWitchExpectation(), PutPieceExpectation())
            else -> listOf(PutPieceExpectation())
        }

    override fun shouldRunRules(state: State): Boolean = !state.mageOrWitchMustBeInstantlyMoved()

    override fun toEvent(state: State): GameEvent = PlaceTile(state.currentTileName(), state.currentPlayerId())
}

class PutPieceExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is PutPiece || command is PickUpAbbot || command is SkipPiece

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when {
        noExpectations -> listOf(PutTileExpectation())
        else -> emptyList()
    }

    override fun toEvent(state: State): GameEvent = SelectPiece
}

class ChooseCornCircleActionExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is ChooseCornCircleActionCommand

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
    override fun expects(command: Command): Boolean = command is AddPieceCommand || command is AvoidCornCircleAction
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = AddPiece(state.currentPlayerId())
}

class RemovePieceExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is RemovePieceCommand || command is AvoidCornCircleAction
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = RemovePiece(state.currentPlayerId())
}

class MoveMagicianOrWitchExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is MoveMagicianOrWitchCommand || command is PickUpMagicianOrWitch
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = PlaceWitchOrMagician
}