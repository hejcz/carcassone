package io.github.hejcz.api

/**
 * Checks if command is valid. Important difference between validator and expectation
 * in game flow is expectation checks if command may be right e.g. when corn action in selected
 * game flow checks if command is add piece, remove piece or avoid making a move. Validator
 * checks if selected action was add if add piece command arrives, does player have a piece
 * in particular place and so on. So basically game state expectation is fail-fast mechanism
 * while validator handles all corner cases.
 */
interface CmdValidator {
    fun validate(state: State, command: Command): Collection<GameEvent>
}