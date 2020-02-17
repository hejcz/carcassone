package io.github.hejcz.core

import io.github.hejcz.core.tile.Tile
import io.github.hejcz.expansion.corncircles.CornSymbol

interface State {
    // getters
    fun currentTile(): Tile
    fun recentPosition(): Position
    fun recentTile(): Tile
    fun tileAt(position: Position): Tile
    fun currentPlayerId(): Long
    fun completedCastle(positionedDirection: PositionedDirection): CompletedCastle?
    fun currentTileName(): String
    fun anyPlayerHasPiece(position: Position, role: Role): Boolean
    fun allKnights(): List<OwnedPiece>
    fun allBrigands(): List<OwnedPiece>
    fun allMonks(): List<OwnedPiece>
    fun allAbbots(): List<OwnedPiece>
    fun allPeasants(): List<OwnedPiece>
    fun currentPlayerPieces(cornSymbol: CornSymbol): List<OwnedPiece>
    fun findPieces(position: Position, role: Role): List<OwnedPiece>
    fun exists(position: Position, direction: Direction, piece: NpcPiece): Boolean
    fun allPlayersIdsStartingWithCurrent(): List<Long>
    fun isAvailableForCurrentPlayer(piece: Piece): Boolean
    fun previousPlayerId(): Long
    fun mageOrWitchMustBeInstantlyMoved(): Boolean
    fun canBePlacedOn(piece: NpcPiece, targetPos: PositionedDirection): Boolean
    fun canBePickedUp(piece: NpcPiece): Boolean
    // mutators
    fun addTile(position: Position, rotation: Rotation): State
    fun addNpcPiece(piece: NpcPiece, position: Position, direction: Direction): State
    fun addPiece(piece: Piece, role: Role): State
    fun addPiece(position: Position, piece: Piece, role: Role): State
    fun removePiece(position: Position, piece: Piece, role: Role): State
    fun returnPieces(pieces: Collection<OwnedPiece>): State
    fun changeActivePlayer(): State
    fun addCompletedCastle(completedCastle: CompletedCastle): State
    fun addCompletedRoad(completedRoad: CompletedRoad): State
}
