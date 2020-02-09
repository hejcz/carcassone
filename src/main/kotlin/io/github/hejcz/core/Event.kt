package io.github.hejcz.core

import io.github.hejcz.magic.MagicTarget

open class GameEvent

// expectations

object BeginEvent : GameEvent()

data class PlaceTile(val tileId: String, val playerId: Long) : GameEvent()

object SelectPiece : GameEvent()

data class AddPiece(val playerId: Long) : GameEvent()

data class RemovePiece(val playerId: Long) : GameEvent()

data class ChooseCornAction(val playerId: Long) : GameEvent()

// events

data class PlayerScored(val playerId: Long, val score: Int, val returnedPieces: Collection<PieceOnBoard>) : GameEvent()

data class PlayerDidNotScore(val playerId: Long, val returnedPieces: Collection<PieceOnBoard>) : GameEvent()

data class CastleFinished(val castle: CompletedCastle) : GameEvent()

data class RoadFinished(val road: CompletedRoad) : GameEvent()

// errors

object InvalidTileLocation : GameEvent()

object InvalidPieceLocation : GameEvent()

object PieceCanNotBeSkipped : GameEvent()

data class PieceCanNotBePickedUp(val magicTarget: MagicTarget) : GameEvent()

object NoAbbotToPickUp : GameEvent()

data class PieceMayNotBeUsedInRole(val piece: Piece, val role: Role) : GameEvent()

data class NoMappleAvailable(val piece: Piece) : GameEvent()

object UnexpectedCommand : GameEvent()
