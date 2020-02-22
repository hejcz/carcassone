package io.github.hejcz.base

import io.github.hejcz.api.GameEvent
import io.github.hejcz.api.Piece
import io.github.hejcz.api.Role

// expectations

data class TileEvent(val tileId: String, val playerId: Long) : GameEvent

object PieceEvent : GameEvent

data class AddPieceEvent(val playerId: Long) : GameEvent

data class RemovePieceEvent(val playerId: Long) : GameEvent

data class ChooseCornActionEvent(val playerId: Long) : GameEvent

interface SystemGameEvent

interface MixedGameEvent

object ScorePointsEvent : GameEvent, SystemGameEvent

object ChangePlayerEvent : GameEvent, SystemGameEvent

object EndGameEvent : GameEvent, SystemGameEvent

// events

data class ScoreEvent(val playerId: Long, val score: Int, val returnedPieces: Collection<PieceOnBoard>) : GameEvent, MixedGameEvent

data class NoScoreEvent(val playerId: Long, val returnedPieces: Collection<PieceOnBoard>) : GameEvent, MixedGameEvent

// errors

object InvalidTileLocationEvent : GameEvent

object InvalidPieceLocationEvent : GameEvent

object CantSkipPieceEvent : GameEvent

object CantPickUpAbbotEvent : GameEvent

data class InvalidPieceRoleEvent(val piece: Piece, val role: Role) : GameEvent

data class NoMeepleEvent(val piece: Piece) : GameEvent

object UnexpectedCommandEvent : GameEvent
