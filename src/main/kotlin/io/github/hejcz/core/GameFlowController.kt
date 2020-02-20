package io.github.hejcz.core

import io.github.hejcz.core.tile.NoTile
import io.github.hejcz.expansion.abbot.PickUpAbbotCmd
import io.github.hejcz.expansion.corncircles.*
import io.github.hejcz.expansion.magic.*

data class FlowState(
    val idOfPlayerMakingMove: Long,
    val gameStarted: Boolean = true,
    val endGameSignaled: Boolean = false,
    val tilePlaced: Boolean = false,
    val piecePlaced: Boolean = false,
    val scoreCalculated: Boolean = false,
    val changedPlayer: Boolean = false,
    // builder
    val extendedBuilder: Boolean = false,
    val usedBuilder: Boolean = false,
    // witch
    val placedWitchTile: Boolean = false,
    val placedWizard: Boolean = false,
    // corn
    val placedCornTile: Boolean = false,
    val chosenCornAction: CornCircleAction? = null,
    val takenCornActions: Int = 0
)

data class GameFlowController(val state: FlowState) {

    fun isOk(command: Command, state: State): Boolean =
        expectation(state).matches(command, this.state)

    fun dispatch(command: Command, state: State): GameFlowController {
        val expectation: NewExpectation = expectation(state)
        return GameFlowController(expectation.newState(command, this.state, state))
    }

    private interface NewExpectation {
        fun matches(command: Command, state: FlowState): Boolean
        fun newState(command: Command, state: FlowState, gameState: State): FlowState
        fun events(gameState: State, state: FlowState): Set<GameEvent>
    }

    private object PlaceTile : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is TileCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            FlowState(state.idOfPlayerMakingMove).copy(
                tilePlaced = true,
                placedCornTile = gameState.currentTile() is CornCircleTile,
                placedWitchTile = gameState.currentTile() is MagicTile,
                extendedBuilder = !state.extendedBuilder && extendsBuilder(gameState)
            )
        override fun events(gameState: State, state: FlowState) = when (val newTile = gameState.currentTile()) {
            is NoTile -> emptySet<GameEvent>()
            else -> setOf(TileEvent(gameState.currentTile().name(), state.idOfPlayerMakingMove))
        }

        // TODO
        private fun extendsBuilder(gameState: State): Boolean = false
    }

    private object PlacePiece : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean =
            command is PieceCmd || command is SkipPieceCmd || command is PickUpAbbotCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(piecePlaced = true)
        override fun events(gameState: State, state: FlowState) = setOf(PieceEvent)
    }

    private object MoveWizard : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean =
            command is MoveMageOrWitchCmd || command is PickUpMageOrWitchCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(placedWizard = true)
        override fun events(gameState: State, state: FlowState) = setOf(PlaceWitchOrMage)
    }

    private object CalculateScore : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is SystemCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(scoreCalculated = true)
        override fun events(gameState: State, state: FlowState) = setOf(ScorePointsEvent)
    }

    private object ChooseCornAction : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is ChooseCornCircleActionCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(chosenCornAction = (command as ChooseCornCircleActionCmd).action,
                idOfPlayerMakingMove = gameState.nextPlayerId(1))
        override fun events(gameState: State, state: FlowState) =
            setOf(ChooseCornActionEvent(state.idOfPlayerMakingMove))
    }

    private object TakeCornAction : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean =
            command is AddPieceCmd && CornCircleAction.ADD_PIECE == state.chosenCornAction
                || command is RemovePieceCmd && CornCircleAction.REMOVE_PIECE == state.chosenCornAction
                || command is AvoidCornCircleActionCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(takenCornActions = state.takenCornActions + 1,
                idOfPlayerMakingMove = gameState.nextPlayerId(1))
        override fun events(gameState: State, state: FlowState) = when (state.chosenCornAction!!) {
            CornCircleAction.ADD_PIECE -> setOf(AddPieceEvent(gameState.nextPlayerId(1)))
            CornCircleAction.REMOVE_PIECE -> setOf(RemovePieceEvent(gameState.nextPlayerId(1)))
        }
    }

    private object ChangePlayer : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is SystemCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(idOfPlayerMakingMove = gameState.nextPlayerId(1), changedPlayer = true)
        override fun events(gameState: State, state: FlowState) = setOf(ChangePlayerEvent)
    }

    private object UseBuilder : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is SystemCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(usedBuilder = true, changedPlayer = true)
        override fun events(gameState: State, state: FlowState) = emptySet<GameEvent>()
    }

    private object EndGame : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is SystemCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(endGameSignaled = true)
        override fun events(gameState: State, state: FlowState) = setOf(EndGameEvent)
    }

    private object StartGame : NewExpectation {
        override fun matches(command: Command, state: FlowState): Boolean = command is BeginCmd
        override fun newState(command: Command, state: FlowState, gameState: State): FlowState =
            state.copy(gameStarted = true)
        override fun events(gameState: State, state: FlowState) = emptySet<GameEvent>()
    }

    private fun expectation(state: State): NewExpectation {
        return when {
            !this.state.gameStarted -> StartGame
            !this.state.tilePlaced -> PlaceTile
            wizardsAreOnTheSameObject(state) -> MoveWizard
            this.state.placedWitchTile && !this.state.placedWizard -> MoveWizard
            !this.state.piecePlaced -> PlacePiece
            !this.state.scoreCalculated -> CalculateScore
            this.state.placedCornTile -> when {
                this.state.chosenCornAction == null -> ChooseCornAction
                this.state.takenCornActions != state.countPlayers() -> TakeCornAction
                else -> newTurn(state)
            }
            else -> newTurn(state)
        }
    }

    private fun newTurn(state: State): NewExpectation = when {
        this.state.extendedBuilder and !this.state.usedBuilder -> UseBuilder
        !this.state.changedPlayer -> ChangePlayer
        state.tilesLeft() == 0 && !this.state.endGameSignaled -> EndGame
        else -> PlaceTile
    }

    private fun wizardsAreOnTheSameObject(state: State): Boolean =
        state.getWizardState()?.mageOrWitchMustBeInstantlyMoved(state) ?: false

    fun events(state: State): Collection<GameEvent> = expectation(state).events(state, this.state)
    fun currentPlayer(): Long = state.idOfPlayerMakingMove
}

