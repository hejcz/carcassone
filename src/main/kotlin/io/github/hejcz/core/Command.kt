package io.github.hejcz.core

interface Command

data class PutTile(val position: Position, val rotation: Rotation) : Command

object Begin : Command

data class PutPiece(val piece: Piece, val pieceRole: PieceRole) : Command

object SkipPiece : Command
