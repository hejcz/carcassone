package io.github.hejcz.core

/**
 * Applies command to game state.
 */
interface CmdHandler {
    /**
     * Checks whether this handler may be applied to specific command.
     */
    fun isApplicableTo(command: Command): Boolean

    /**
     * Modifies game state according to command.
     */
    fun apply(state: State, command: Command): State
}

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

/**
 * Rules rewarding players with points during normals coring phase.
 */
interface Scoring {
    fun apply(command: Command, state: State): Collection<GameEvent>
}

/**
 * Rules rewarding players with points when the game ends.
 */
interface EndGameScoring {
    fun apply(state: State): Collection<GameEvent>

}
