package io.github.hejcz

import io.github.hejcz.mapples.PieceId

sealed class GameEvent

data class OccupiedAreaCompleted(val playerId: Long, val returnedPiecesIds: Collection<PieceId>) : GameEvent()

data class PlayerScored(val playerId: Long, val score: Int, val returnedPiecesIds: Collection<PieceId>) : GameEvent()

data class PlaceTile(val tileId: String, val playerId: Long) : GameEvent()

object SelectPiece : GameEvent()

object TilePlacedInInvalidPlace : GameEvent()

object PiecePlacedInInvalidPlace : GameEvent()

data class NoMappleAvailable(val pieceId: PieceId) : GameEvent()