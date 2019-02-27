package io.github.hejcz.rules.helpers

import io.github.hejcz.engine.State
import io.github.hejcz.mapples.PieceOnBoard
import io.github.hejcz.mapples.PieceRole

fun State.filterPieces(test: (PieceRole) -> Boolean): List<PieceOnBoard> =
    this.players.flatMap { player -> player.pieces().filter { piece -> test(piece.role) } }