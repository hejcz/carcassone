package io.github.hejcz.expansion.magic

import io.github.hejcz.core.*
import io.github.hejcz.core.setup.*
import io.github.hejcz.core.commandValidator
import io.github.hejcz.core.eventsHandler

object MagicianAndWitchExtension : Extension {

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(MaHeA, MaHeB, MaHeC, MaHeD, MaHeE, MaHeF, MaHeG, MaHeH)
    }

    override fun modify(validatorsSetup: ValidatorsSetup) {
        validatorsSetup.add(MoveMagicianOrWitchCommandValidator)
        validatorsSetup.add(PickUpMagicianOrWitchValidator)
    }

    override fun modify(commandHandlersSetup: CommandHandlersSetup) {
        commandHandlersSetup.add(MoveMagicianOrWitchCommandHandler)
        commandHandlersSetup.add(PickUpMagicianOrWitchHandler)
    }

    private val MoveMagicianOrWitchCommandHandler =
        eventsHandler<MoveMagicianOrWitchCmd> { state, command ->
            GameChanges.noEvents(
                state.addNpcPiece(
                    command.magicTarget.toPiece(),
                    command.position,
                    command.direction
                )
            )
        }

    private val MoveMagicianOrWitchCommandValidator =
        commandValidator<MoveMagicianOrWitchCmd> { state, command ->
            if (state.canBePlacedOn(
                    command.magicTarget.toPiece(),
                    PositionedDirection(command.position, command.direction)
                )
            ) {
                emptySet()
            } else {
                setOf(InvalidPieceLocationEvent)
            }
        }

    private val PickUpMagicianOrWitchHandler =
        eventsHandler<PickUpMagicianOrWitchCmd>()

    private val PickUpMagicianOrWitchValidator =
        commandValidator<PickUpMagicianOrWitchCmd> { state, command ->
            if (state.canBePickedUp(command.magicTarget.toPiece())) {
                emptySet()
            } else {
                setOf(CantPickUpPieceEvent(command.magicTarget))
            }
        }
}
