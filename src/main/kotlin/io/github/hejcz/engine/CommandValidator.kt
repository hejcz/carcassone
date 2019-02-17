package io.github.hejcz.engine

import io.github.hejcz.Command
import io.github.hejcz.GameEvent

interface CommandValidator {

    fun validate(state: State, command: Command): Collection<GameEvent>

}