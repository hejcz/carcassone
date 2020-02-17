package io.github.hejcz.core

import io.github.hejcz.expansion.magic.MagicTarget

open class GameEvent

// expectations

object BeginEvent : GameEvent()

data class TileEvent(val tileId: String, val playerId: Long) : GameEvent()

object PieceEvent : GameEvent()

data class AddPieceEvent(val playerId: Long) : GameEvent()

data class RemovePieceEvent(val playerId: Long) : GameEvent()

data class ChooseCornActionEvent(val playerId: Long) : GameEvent()

interface SystemGameEvent

interface MixedGameEvent

object ScorePointsEvent : GameEvent(), SystemGameEvent

object ChangePlayerEvent : GameEvent(), SystemGameEvent

object EndGameEvent : GameEvent(), SystemGameEvent

// events

data class ScoreEvent(val playerId: Long, val score: Int, val returnedPieces: Collection<PieceOnBoard>) : GameEvent(), MixedGameEvent

data class NoScoreEvent(val playerId: Long, val returnedPieces: Collection<PieceOnBoard>) : GameEvent(), MixedGameEvent

data class CastleClosedEvent(val castle: CompletedCastle) : GameEvent(), SystemGameEvent

data class RoadClosedEvent(val road: CompletedRoad) : GameEvent(), SystemGameEvent

// errors

object InvalidTileLocationEvent : GameEvent()

object InvalidPieceLocationEvent : GameEvent()

object CantSkipPieceEvent : GameEvent()

data class CantPickUpPieceEvent(val magicTarget: MagicTarget) : GameEvent()

object CantPickUpAbbotEvent : GameEvent()

data class InvalidPieceRoleEvent(val piece: Piece, val role: Role) : GameEvent()

data class NoMappleEvent(val piece: Piece) : GameEvent()

object UnexpectedCommandEvent : GameEvent()
