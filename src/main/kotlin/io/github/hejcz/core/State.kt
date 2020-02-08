package io.github.hejcz.core

import io.github.hejcz.core.tile.Tile
import io.github.hejcz.corncircles.CornSymbol

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
    fun allKnights(): List<Pair<Long, PieceOnBoard>>
    fun allBrigands(): List<Pair<Long, PieceOnBoard>>
    fun allMonks(): List<Pair<Long, PieceOnBoard>>
    fun allAbbots(): List<Pair<Long, PieceOnBoard>>
    fun allPeasants(): List<Pair<Long, PieceOnBoard>>
    fun currentPlayerPieces(cornSymbol: CornSymbol): List<Pair<Long, PieceOnBoard>>
    fun findPieces(position: Position, role: Role): List<Pair<Long, PieceOnBoard>>
    fun allPlayersIdsStartingWithCurrent(): List<Long>
    fun isAvailableForCurrentPlayer(piece: Piece): Boolean
    fun previousPlayerId(): Long
    // mutators
    fun addTile(position: Position, rotation: Rotation): State
    fun addPiece(piece: Piece, role: Role): State
    fun addPiece(position: Position, piece: Piece, role: Role): State
    fun removePiece(position: Position, piece: Piece, role: Role): State
    fun returnPieces(pieces: Collection<OwnedPiece>): State
    fun changeActivePlayer(): State
    fun addCompletedCastle(completedCastle: CompletedCastle): State
}