package io.github.hejcz.corncircles

import io.github.hejcz.core.*

enum class CornCircleAction {
    ADD_PIECE, REMOVE_PIECE
}

data class ChooseCornCircleAction(val action: CornCircleAction): Command

data class AddPiece(val position: Position, val piece: Piece, val role: Role): Command

data class RemovePiece(val position: Position, val piece: Piece, val role: Role): Command
