package io.github.hejcz.core

interface Command

/**
 * Starts the game.
 */
object BeginCmd : Command

/**
 * Places tile on the board.
 */
data class TileCmd(val position: Position, val rotation: Rotation) : Command

/**
 * Places piece on the board.
 */
data class PieceCmd(val piece: Piece, val role: Role) : Command

/**
 * Skip placing piece.
 */
object SkipPieceCmd : Command

/**
 * Artificial command allows system to advance game controller when no user interaction is needed.
 */
object SystemCmd : Command

class UnexpectedCommandException : Throwable()

data class NoHandlerForCommand(val command: Command) : Throwable()
