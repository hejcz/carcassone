package io.github.hejcz.core.setup

open class GameSetup(private vararg val extensions: Extension) {

    open fun tiles() = TilesSetup().withExtensions(*extensions).remainingTiles()

    open fun validators() = ValidatorsSetup().withExtensions(*extensions).validators()

    open fun pieces() = PiecesSetup().withExtensions(*extensions).pieces()

    open fun rules() = RulesSetup().withExtensions(*extensions).rules()

    open fun endRules() = RulesSetup().withExtensions(*extensions).endRules()

    open fun handlers() = CommandHandlersSetup().withExtensions(*extensions).handlers()
}
