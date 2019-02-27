package io.github.hejcz.engine

open class GameSetup(private vararg val extensions: Extension) {

    open fun tiles() = DefaultDeck().withExtensions(*extensions).remainingTiles()

    open fun validators() = DefaultValidators().withExtensions(*extensions).validators()

}