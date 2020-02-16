package io.github.hejcz.magic

import io.github.hejcz.core.*
import io.github.hejcz.setup.*
import io.github.hejcz.util.commandValidator
import io.github.hejcz.util.eventsHandler

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

    private val MoveMagicianOrWitchCommandHandler = eventsHandler<MoveMagicianOrWitchCommand> { state, command ->
        GameChanges.noEvents(
            state.addNpcPiece(
                command.magicTarget.toPiece(),
                command.position,
                command.direction
            )
        )
    }

    private val MoveMagicianOrWitchCommandValidator = commandValidator<MoveMagicianOrWitchCommand> { state, command ->
        if (state.canBePlacedOn(command.magicTarget.toPiece(), PositionedDirection(command.position, command.direction))) {
            emptySet()
        } else {
            setOf(InvalidPieceLocation)
        }
    }

    private val PickUpMagicianOrWitchHandler = eventsHandler<PickUpMagicianOrWitch>()

    private val PickUpMagicianOrWitchValidator = commandValidator<PickUpMagicianOrWitch> { state, command ->
        if (state.canBePickedUp(command.magicTarget.toPiece())) {
            emptySet()
        } else {
            setOf(PieceCanNotBePickedUp(command.magicTarget))
        }
    }

}