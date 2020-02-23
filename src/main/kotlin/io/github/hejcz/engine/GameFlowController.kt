package io.github.hejcz.engine

import io.github.hejcz.api.Command
import io.github.hejcz.api.GameEvent
import io.github.hejcz.api.State
import io.github.hejcz.base.*
import io.github.hejcz.base.tile.NoTile
import io.github.hejcz.components.abbot.PickUpAbbotCmd
import io.github.hejcz.components.corncircles.*
import io.github.hejcz.components.magic.*

data class FlowState(
    val idOfPlayerMakingMove: Long,
    val gameStarted: Boolean = true,
    val endGameSignaled: Boolean = false,
    val tilePlaced: Boolean = false,
    val piecePlaced: Boolean = false,
    val scoreCalculated: Boolean = false,
    val changedPlayer: Boolean = false,
    // builder
    val checkedBuilder: Boolean = false,
    val usedBuilder: Boolean = false,
    // witch
    val placedWitchTile: Boolean = false,
    val placedWizard: Boolean = false,
    // corn
    val placedCornTile: Boolean = false,
    val chosenCornAction: CornCircleAction? = null,
    val takenCornActions: Int = 0
)

data class GameFlowController(val flowState: FlowState) {

    /**
     * Checks if command matches expectation. Command is not applied to state.
     */
    fun isOk(command: Command, gameState: State): Boolean =
        expectation(gameState).matches(command)

    /**
     * Advances flow. Command is not applied to state so it matches isOk method. This is possible
     * that isOk state returns different expectation for old and new state.
     */
    fun dispatch(command: Command, gameState: State): GameFlowController {
        val expectation: Stage = expectation(gameState)
        return GameFlowController(
            expectation.newState(command, flowState, gameState)
        )
    }

    /**
     * Creates event for new expectation. It receives state with command applied.
     */
    fun events(gameState: State): Collection<GameEvent> = expectation(gameState).events(gameState, flowState)

    private fun expectation(gameState: State): Stage {
        return when {
            !flowState.gameStarted -> StartGame
            !flowState.tilePlaced -> PlaceTile
            gameState.wizardsAreOnTheSameObject() -> MoveWizard
            flowState.placedWitchTile && !flowState.placedWizard -> MoveWizard
            !flowState.piecePlaced -> PlacePiece
            !flowState.scoreCalculated -> CalculateScore
            flowState.placedCornTile -> when {
                flowState.chosenCornAction == null -> ChooseCornAction
                flowState.takenCornActions != gameState.countPlayers() -> TakeCornAction
                else -> newTurn(gameState)
            }
            else -> newTurn(gameState)
        }
    }

    private fun newTurn(gameState: State): Stage = when {
        !flowState.checkedBuilder -> when {
            !flowState.usedBuilder and gameState.extendedBuilder() -> UseBuilder
            else -> CleanBuilder
        }
        !flowState.changedPlayer -> ChangePlayer
        gameState.tilesLeft() == 0 && !flowState.endGameSignaled -> EndGame
        else -> PlaceTile
    }

    private fun State.wizardsAreOnTheSameObject(): Boolean =
        this.getWizardState()?.mageOrWitchMustBeInstantlyMoved(this) ?: false

    private fun State.extendedBuilder(): Boolean =
        this.getBuilderState()?.extendedBuilder(this) ?: false

    fun currentPlayer(): Long = flowState.idOfPlayerMakingMove

    private interface Stage {
        fun matches(command: Command): Boolean
        fun newState(command: Command, flowState: FlowState, gameState: State): FlowState
        fun events(gameState: State, flowState: FlowState): Set<GameEvent>
    }

    private object PlaceTile : Stage {
        override fun matches(command: Command): Boolean = command is TileCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            FlowState(flowState.idOfPlayerMakingMove).copy(
                tilePlaced = true,
                placedCornTile = gameState.currentTile() is CornCircleTile,
                placedWitchTile = gameState.currentTile() is MagicTile,
                usedBuilder = flowState.usedBuilder
            )
        override fun events(gameState: State, flowState: FlowState) = when (val tile = gameState.currentTile()) {
            is NoTile -> emptySet<GameEvent>()
            else -> setOf(TileEvent(tile.name(), flowState.idOfPlayerMakingMove))
        }
    }

    private object PlacePiece : Stage {
        override fun matches(command: Command): Boolean =
            command is PieceCmd || command is SkipPieceCmd || command is PickUpAbbotCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(piecePlaced = true)
        override fun events(gameState: State, flowState: FlowState) = setOf(
            PieceEvent
        )
    }

    private object MoveWizard : Stage {
        override fun matches(command: Command): Boolean =
            command is MoveMageOrWitchCmd || command is PickUpMageOrWitchCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(placedWizard = true)
        override fun events(gameState: State, flowState: FlowState) = setOf(PlaceWitchOrMage)
    }

    private object CalculateScore : Stage {
        override fun matches(command: Command): Boolean = command is SystemCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(scoreCalculated = true)
        override fun events(gameState: State, flowState: FlowState) = setOf(
            ScorePointsEvent
        )
    }

    private object ChooseCornAction : Stage {
        override fun matches(command: Command): Boolean = command is ChooseCornCircleActionCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(chosenCornAction = (command as ChooseCornCircleActionCmd).action,
                idOfPlayerMakingMove = gameState.nextPlayerId(1))
        override fun events(gameState: State, flowState: FlowState) =
            setOf(ChooseCornActionEvent(flowState.idOfPlayerMakingMove))
    }

    private object TakeCornAction : Stage {
        override fun matches(command: Command): Boolean =
            command is AddPieceCmd || command is RemovePieceCmd || command is AvoidCornCircleActionCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(takenCornActions = flowState.takenCornActions + 1, idOfPlayerMakingMove = gameState.nextPlayerId(1))
        override fun events(gameState: State, flowState: FlowState) = when (flowState.chosenCornAction!!) {
            CornCircleAction.ADD_PIECE -> setOf(
                AddPieceEvent(gameState.nextPlayerId(1))
            )
            CornCircleAction.REMOVE_PIECE -> setOf(
                RemovePieceEvent(gameState.nextPlayerId(1))
            )
        }
    }

    private object ChangePlayer : Stage {
        override fun matches(command: Command): Boolean = command is SystemCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(idOfPlayerMakingMove = gameState.nextPlayerId(1), changedPlayer = true)
        override fun events(gameState: State, flowState: FlowState) = setOf(
            ChangePlayerEvent
        )
    }

    private object UseBuilder : Stage {
        override fun matches(command: Command): Boolean = command is SystemCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(usedBuilder = true, changedPlayer = true, checkedBuilder = true)
        override fun events(gameState: State, flowState: FlowState) = emptySet<GameEvent>()
    }

    private object CleanBuilder : Stage {
        override fun matches(command: Command): Boolean = command is SystemCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(usedBuilder = false, checkedBuilder = true)
        override fun events(gameState: State, flowState: FlowState) = emptySet<GameEvent>()
    }

    private object EndGame : Stage {
        override fun matches(command: Command): Boolean = command is SystemCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(endGameSignaled = true)
        override fun events(gameState: State, flowState: FlowState) = setOf(
            EndGameEvent
        )
    }

    private object StartGame : Stage {
        override fun matches(command: Command): Boolean = command is BeginCmd
        override fun newState(command: Command, flowState: FlowState, gameState: State): FlowState =
            flowState.copy(gameStarted = true)
        override fun events(gameState: State, flowState: FlowState) = emptySet<GameEvent>()
    }
}
