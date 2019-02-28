package io.github.hejcz.setup

interface Extension {
    fun modify(deck: TilesSetup) = Unit
    fun modify(validatorsSetup: ValidatorsSetup) = Unit
    fun modify(piecesSetup: PiecesSetup) = Unit
    fun modify(rulesSetup: RulesSetup) = Unit
    fun modify(commandHandlersSetup: CommandHandlersSetup) = Unit
}
