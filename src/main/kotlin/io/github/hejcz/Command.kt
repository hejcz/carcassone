package io.github.hejcz

import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.Position
import io.github.hejcz.placement.Rotation

sealed class Command

data class PutTile(val position: Position, val rotation: Rotation) : Command()

object Begin : Command()

data class PutPiece(val pieceId: PieceId, val pieceRole: PieceRole) : Command()

object SkipPiece : Command()