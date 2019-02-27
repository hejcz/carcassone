package io.github.hejcz.abbot

import io.github.hejcz.core.AbbotPiece
import io.github.hejcz.setup.*

object AbbotExtension : Extension {
    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(AbbotPiece)
    override fun modify(commandHandlersSetup: CommandHandlersSetup) = commandHandlersSetup.add(PickUpAbbotHandler)
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.add(AbbotInGardenScoredRule, AbbotPickedUpRule)
        rulesSetup.add(IncompleteGardenRule)
    }
    override fun modify(validatorsSetup: ValidatorsSetup) = validatorsSetup.add(PickUpAbbotValidator)
}
