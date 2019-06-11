package io.github.hejcz.core

sealed class GameEvent

data class OccupiedAreaCompleted(val playerId: Long, val returnedPieces: Collection<PieceOnBoard>) : GameEvent()

data class PlayerScored(val playerId: Long, val score: Int, val returnedPieces: Collection<PieceOnBoard>) : GameEvent()

data class PlaceTile(val tileId: String, val playerId: Long) : GameEvent()

object SelectPiece : GameEvent()

object TilePlacedInInvalidPlace : GameEvent()

object PiecePlacedInInvalidPlace : GameEvent()

object NoAbbotToPickUp : GameEvent()

data class PieceMayNotBeUsedInARole(val piece: Piece, val role: Role) : GameEvent()

data class NoMappleAvailable(val piece: Piece) : GameEvent()

object InvalidCommand : GameEvent()
