package io.github.hejcz.core

import io.github.hejcz.expansion.abbot.*
import io.github.hejcz.expansion.corncircles.*
import io.github.hejcz.expansion.magic.MagicTile
import io.github.hejcz.expansion.magic.MoveMagicianOrWitchCmd
import io.github.hejcz.expansion.magic.PickUpMagicianOrWitchCmd
import io.github.hejcz.expansion.magic.PlaceWitchOrMagician

interface Expectation {
    fun expects(command: Command): Boolean
    fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation>
    fun toEvent(state: State): GameEvent
    fun shouldRunRules(state: State): Boolean = true
}

class BeginExpectation : Expectation {
    override fun expects(command: Command) = command is BeginCmd
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> =
        listOf(PutTileExpectation())

    override fun toEvent(state: State): GameEvent = BeginEvent
}

class PutTileExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is TileCmd

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> =
        when {
            state.recentTile() is CornCircleTile -> listOf(PutPieceExpectation(), ChooseCornCircleActionExpectation())
            state.recentTile() is MagicTile || state.mageOrWitchMustBeInstantlyMoved() ->
                listOf(MoveMagicianOrWitchExpectation(), PutPieceExpectation())
            else -> listOf(PutPieceExpectation())
        }

    override fun shouldRunRules(state: State): Boolean = !state.mageOrWitchMustBeInstantlyMoved()

    override fun toEvent(state: State): GameEvent = TileEvent(state.currentTileName(), state.currentPlayerId())
}

class PutPieceExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is PieceCmd || command is PickUpAbbotCmd || command is SkipPieceCmd

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = when {
        noExpectations -> listOf(PutTileExpectation())
        else -> emptyList()
    }

    override fun toEvent(state: State): GameEvent = PieceEvent
}

class ChooseCornCircleActionExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is ChooseCornCircleActionCmd

    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> {
        val cmd = command as ChooseCornCircleActionCmd
        return when (cmd.action) {
            CornCircleAction.ADD_PIECE -> state.allPlayersIdsStartingWithCurrent().map { AddPieceExpectation() } + PutTileExpectation()
            CornCircleAction.REMOVE_PIECE -> state.allPlayersIdsStartingWithCurrent().map { RemovePieceExpectation() } + PutTileExpectation()
        }
    }

    override fun toEvent(state: State): GameEvent = ChooseCornActionEvent(state.currentPlayerId())
}

class AddPieceExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is AddPieceCmd || command is AvoidCornCircleActionCmd
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = AddPieceEvent(state.currentPlayerId())
}

class RemovePieceExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is RemovePieceCmd || command is AvoidCornCircleActionCmd
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = RemovePieceEvent(state.currentPlayerId())
}

class MoveMagicianOrWitchExpectation : Expectation {
    override fun expects(command: Command): Boolean = command is MoveMagicianOrWitchCmd || command is PickUpMagicianOrWitchCmd
    override fun next(command: Command, state: State, noExpectations: Boolean): List<Expectation> = emptyList()
    override fun toEvent(state: State): GameEvent = PlaceWitchOrMagician
}
