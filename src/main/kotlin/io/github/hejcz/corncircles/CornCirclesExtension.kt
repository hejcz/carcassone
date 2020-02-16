package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.setup.*
import io.github.hejcz.util.commandValidator

object CornCirclesExtension : Extension {

    override fun modify(validatorsSetup: ValidatorsSetup) {
        validatorsSetup.add(AvoidCornCircleActionValidator)
        validatorsSetup.add(AddPieceValidator)
        validatorsSetup.add(RemovePieceValidator)
    }

    override fun modify(commandHandlersSetup: CommandHandlersSetup) {
        commandHandlersSetup.add(AddPieceHandler)
        commandHandlersSetup.add(RemovePieceHandler)
        commandHandlersSetup.add(ChooseCornCircleActionHandler)
        commandHandlersSetup.add(AvoidCornCircleActionHandler)
    }

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(Korn1, Korn2, Korn3, Korn4, Korn5, Korn6)
    }

    private val AvoidCornCircleActionHandler = object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is AvoidCornCircleActionCmd

        override fun beforeScoring(state: State, command: Command): GameChanges =
            (command as AvoidCornCircleActionCmd).let {
                GameChanges.noEvents(
                    state.changeActivePlayer()
                )
            }
    }

    private val AvoidCornCircleActionValidator = commandValidator<AvoidCornCircleActionCmd> { state, _ ->
        when (val tile = state.recentTile()) {
            is CornCircleTile -> when {
                state.currentPlayerPieces(tile.cornCircleEffect()).isNotEmpty() -> setOf(CantSkipPieceEvent)
                else -> emptySet()
            }
            else -> emptySet()
        }
    }

    private val AddPieceHandler = object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is AddPieceCmd

        override fun beforeScoring(state: State, command: Command): GameChanges =
            (command as AddPieceCmd).let {
                GameChanges.noEvents(
                    state.addPiece(command.position, command.piece, command.role)
                        .changeActivePlayer()
                )
            }
    }

    private val AddPieceValidator = commandValidator<AddPieceCmd> { state, command ->
        when (val tile = state.recentTile()) {
            is CornCircleTile -> when {
                !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocationEvent)
                playerDoesNotHaveAnyPieceThere(state, command) -> setOf(InvalidPieceLocationEvent)
                else -> emptySet()
            }
            else -> emptySet()
        }
    }

    private val ChooseCornCircleActionHandler = object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is ChooseCornCircleActionCmd

        override fun beforeScoring(state: State, command: Command): GameChanges =
            (command as ChooseCornCircleActionCmd).let {
                GameChanges.noEvents(
                    state.changeActivePlayer()
                )
            }
    }

    private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPieceCmd) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.playerId }

    private val RemovePieceHandler = object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is RemovePieceCmd

        override fun beforeScoring(state: State, command: Command): GameChanges =
            (command as RemovePieceCmd).let {
                GameChanges.noEvents(
                    state.removePiece(command.position, command.piece, command.role)
                        .changeActivePlayer()
                )
            }
    }

    private val RemovePieceValidator = commandValidator<RemovePieceCmd> { state, command ->
        when (val tile = state.recentTile()) {
            is CornCircleTile -> when {
                !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocationEvent)
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