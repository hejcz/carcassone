package io.github.hejcz.corncircles

import io.github.hejcz.setup.*

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
}