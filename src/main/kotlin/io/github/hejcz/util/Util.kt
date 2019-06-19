package io.github.hejcz.util

import io.github.hejcz.core.*

inline fun <reified T> eventHandler(crossinline handler: (game: Game, command: T) -> Collection<GameEvent>) =
    object : CommandHandler {
        override fun handle(game: Game, command: Command): Collection<GameEvent> = handler(game, command as T)
        override fun isApplicableTo(command: Command): Boolean = command is T
    }

inline fun <reified T> commandValidator(crossinline handler: (state: State, command: T) -> Collection<GameEvent>) =
    object : CommandValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> =
            (command as? T)?.let { handler(state, command) } ?: emptySet()
    }

inline fun <reified T> eventHandlerNoEvents(crossinline handler: (game: Game, command: T) -> Unit) =
    object : CommandHandler {
        override fun handle(game: Game, command: Command): Collection<GameEvent> {
            handler(game, command as T)
            return emptySet()
        }
        override fun isApplicableTo(command: Command): Boolean = command is T
    }