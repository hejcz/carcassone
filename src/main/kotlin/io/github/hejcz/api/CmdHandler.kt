package io.github.hejcz.api

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