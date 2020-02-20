package io.github.hejcz.core

import io.github.hejcz.core.tile.Tile
import io.github.hejcz.expansion.corncircles.CornSymbol
import io.github.hejcz.expansion.magic.MageAndWitchState
import kotlin.reflect.KClass

interface StateExtension {
    fun id(): StateExtensionId
}

inline class StateExtensionId(val id: String)

interface ExtendableState {
    fun update(extension: StateExtension): State
    fun get(id: StateExtensionId): StateExtension?
}

interface State : ExtendableState {
    // getters
    fun currentTile(): Tile
    fun recentPosition(): Position
    fun recentTile(): Tile
    fun tileAt(position: Position): Tile
    fun currentPlayerId(): Long
    fun nextPlayerId(i: Int): Long
    fun countPlayers(): Int
    fun completedCastle(positionedDirection: PositionedDirection): CompletedCastle?
    fun currentTileName(): String
    fun anyPlayerHasPiece(position: Position, role: Role): Boolean
    fun all(clazz: KClass<out Role>): List<OwnedPiece>
    fun currentPlayerPieces(cornSymbol: CornSymbol): List<OwnedPiece>
    fun findPieces(position: Position, role: Role): List<OwnedPiece>
    fun allPlayersIdsStartingWithCurrent(): List<Long>
    fun isAvailableForCurrentPlayer(piece: Piece): Boolean
    fun previousPlayerId(): Long
    fun findOpenCastles(): Set<PositionedDirection>
    fun findOpenRoads(): Set<PositionedDirection>
    fun tilesLeft(): Int
    fun getNewCompletedCastles(): List<Castle>
    fun getNewCompletedRoads(): List<Road>
    // mutators
    fun addTile(position: Position, rotation: Rotation): State
    fun addPiece(piece: Piece, role: Role): State
    fun addPiece(position: Position, piece: Piece, role: Role): State
    fun removePiece(position: Position, piece: Piece, role: Role): State
    fun returnPieces(pieces: Collection<OwnedPiece>): State
    fun changeActivePlayer(): State
    fun setCurrentPlayer(currentPlayer: Long): State
}
