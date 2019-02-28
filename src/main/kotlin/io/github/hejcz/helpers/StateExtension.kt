package io.github.hejcz.helpers

import io.github.hejcz.core.PieceOnBoard
import io.github.hejcz.core.PieceRole
import io.github.hejcz.core.State

fun State.filterPieces(test: (PieceRole) -> Boolean): List<PieceOnBoard> =
    this.players.flatMap { player -> player.pieces().filter { piece -> test(piece.role) } }
