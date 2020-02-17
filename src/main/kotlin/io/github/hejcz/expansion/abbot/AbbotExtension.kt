package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.setup.*

object AbbotExtension : Extension {
    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(AbbotPiece)
    override fun modify(commandHandlersSetup: CommandHandlersSetup) = commandHandlersSetup.add(PickUpAbbotHandler)
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.add(GardenCompletedRule, AbbotPickedUp)
        rulesSetup.add(RewardIncompleteGardens)
    }

    override fun modify(validatorsSetup: ValidatorsSetup) = validatorsSetup.add(PickUpAbbotValidator)
}
