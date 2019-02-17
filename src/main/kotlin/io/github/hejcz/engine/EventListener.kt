package io.github.hejcz.engine

import io.github.hejcz.GameEvent

interface EventListener {

    fun handle(state: State, event: GameEvent)

}