package io.github.hejcz.rules

import io.github.hejcz.GameEvent
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.Position

interface Rule {
    fun afterTilePlaced(position: Position, state: State): Collection<GameEvent>
    fun afterPiecePlaced(state: State, pieceId: PieceId, pieceRole: PieceRole): Collection<GameEvent>
}