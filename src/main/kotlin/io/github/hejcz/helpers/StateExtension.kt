package io.github.hejcz.helpers

import io.github.hejcz.core.*

fun State.filterPieces(test: (Role) -> Boolean): List<PieceOnBoard> =
    this.players.flatMap { player -> player.piecesOnBoard().filter { piece -> test(piece.role) } }
