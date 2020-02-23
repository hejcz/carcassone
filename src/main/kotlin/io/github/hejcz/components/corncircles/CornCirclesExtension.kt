package io.github.hejcz.components.corncircles

import io.github.hejcz.api.*
import io.github.hejcz.base.*
import io.github.hejcz.components.magic.PickUpMageOrWitchCmd
import io.github.hejcz.setup.*

object CornCirclesExtension : Extension {

    override fun modify(setup: ValidatorsSetup) {
        setup.add(AvoidCornCircleActionValidator)
        setup.add(AddPieceValidator)
        setup.add(RemovePieceValidator)
    }

    override fun modify(setup: CommandHandlersSetup) {
        setup.add(AddPieceHandler)
        setup.add(RemovePieceHandler)
        setup.add(ChooseCornCircleActionHandler)
        setup.add(AvoidCornCircleActionHandler)
    }

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(Korn1, Korn2, Korn3, Korn4, Korn5, Korn6)
    }

    override fun modify(setup: StateExtensionSetup) {
        setup.add(StateExt(CornCircleAction.ADD_PIECE))
    }

    private data class StateExt(val selected: CornCircleAction) : StateExtension {
        override fun id(): StateExtensionId = ID

        fun makeDecision(state: State, action: CornCircleAction): State =
            state.update(copy(selected = action))

        fun isSelected(action: CornCircleAction) = action == selected

        fun currentPlayerPieces(state: State, symbol: CornSymbol) = when (symbol) {
            KnightSymbol -> state.allOf(Knight::class, state.currentPlayerId())
            BrigandSymbol -> state.allOf(Brigand::class, state.currentPlayerId())
            PeasantSymbol -> state.allOf(Peasant::class, state.currentPlayerId())
        }

        companion object {
            val ID = StateExtensionId(
                CornCirclesExtension::class.java.simpleName
            )
        }
    }

    private fun State.cornState() = this.get(StateExt.ID)!! as StateExt

    private val AvoidCornCircleActionHandler = cmdHandler<AvoidCornCircleActionCmd>()

    private val AvoidCornCircleActionValidator =
        commandValidator<AvoidCornCircleActionCmd> { state, _ ->
            when (val tile = state.recentTile()) {
                is CornCircleTile -> when {
                    state.cornState()
                        .currentPlayerPieces(state, tile.cornCircleEffect())
                        .isNotEmpty() -> setOf(CantSkipPieceEvent)
                    else -> emptySet()
                }
                else -> emptySet()
            }
        }

    private val AddPieceHandler = object : CmdHandler {
        override fun isApplicableTo(command: Command): Boolean = command is AddPieceCmd

        override fun apply(state: State, command: Command): GameChanges =
            GameChanges.withState(
                (command as AddPieceCmd).let {
                    state.addPiece(command.position, command.piece, command.role)
                }
            )
    }

    private val AddPieceValidator =
        commandValidator<AddPieceCmd> { state, command ->
            when (val tile = state.recentTile()) {
                is CornCircleTile -> when {
                    !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocationEvent)
                    state.cornState().isSelected(CornCircleAction.REMOVE_PIECE) ->
                        setOf(PlayerSelectedOtherCornAction)
                    playerDoesNotHaveAnyPieceThere(state, command) -> setOf(InvalidPieceLocationEvent)
                    !state.isAvailableForCurrentPlayer(command.piece) -> setOf(NoMeepleEvent(command.piece))
                    else -> emptySet()
                }
                else -> emptySet()
            }
        }

    private val ChooseCornCircleActionHandler = object : CmdHandler {
        override fun isApplicableTo(command: Command): Boolean = command is ChooseCornCircleActionCmd

        override fun apply(state: State, command: Command): GameChanges =
            GameChanges.withState(
                state.cornState()
                    .makeDecision(state, (command as ChooseCornCircleActionCmd).action)
            )
    }

    private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPieceCmd) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.playerId }

    private val RemovePieceHandler = object : CmdHandler {
        override fun isApplicableTo(command: Command): Boolean = command is RemovePieceCmd

        override fun apply(state: State, command: Command): GameChanges =
            GameChanges(
                (command as RemovePieceCmd).let {
                    state.removePiece(command.position, command.piece, command.role)
                },
                setOf(PieceRemoved(state.currentPlayerId(), command.position, command.piece, command.role))
            )
    }

    private val RemovePieceValidator =
        commandValidator<RemovePieceCmd> { state, command ->
            when (val tile = state.recentTile()) {
                is CornCircleTile -> when {
                    !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocationEvent)
                    state.cornState().isSelected(CornCircleAction.ADD_PIECE) ->
                        setOf(PlayerSelectedOtherCornAction)
                    playerDoesNotHaveSuchPieceThere(state, command) -> setOf(InvalidPieceLocationEvent)
                    else -> emptySet()
                }
                else -> emptySet()
            }
        }

    private fun playerDoesNotHaveSuchPieceThere(state: State, command: RemovePieceCmd) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.playerId && it.pieceOnBoard.piece == command.piece }
}
