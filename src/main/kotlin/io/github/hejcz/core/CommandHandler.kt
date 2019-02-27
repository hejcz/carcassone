package io.github.hejcz.core

interface CommandHandler {
    fun handle(game: Game, command: Command): Collection<GameEvent>
    fun isApplicableTo(command: Command): Boolean
}
