package io.github.hejcz.engine

sealed class Extension {
    abstract fun modify(deck: DefaultDeck)
    abstract fun modify(validators: DefaultValidators)
}
