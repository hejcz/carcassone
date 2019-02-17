package io.github.hejcz.rules

import io.github.hejcz.GameEvent
import io.github.hejcz.engine.State

interface EndRule {
    fun apply(state: State): Collection<GameEvent>
}