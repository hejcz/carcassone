package io.github.hejcz.core

interface EventListener {

    fun handle(state: State, event: GameEvent)
}
