package io.github.hejcz.core

import io.github.hejcz.expansion.corncircles.*

data class PiecesOnBoard(
    private val knights: List<OwnedPiece> = emptyList(),
    private val brigands: List<OwnedPiece> = emptyList(),
    private val abbots: List<OwnedPiece> = emptyList(),
    private val monks: List<OwnedPiece> = emptyList(),
    private val peasants: List<OwnedPiece> = emptyList(),
    private val npc: List<NPCOnBoard> = emptyList()
) {

    fun put(player: IPlayer, recentPosition: Position, piece: Piece, role: Role): PiecesOnBoard {
        val pieceOnBoard = OwnedPiece(player.id, PieceOnBoard(recentPosition, piece, role))
        return when (role) {
            is Knight -> copy(knights = knights + pieceOnBoard)
            is Brigand -> copy(brigands = brigands + pieceOnBoard)
            is Peasant -> copy(peasants = peasants + pieceOnBoard)
            Monk -> copy(monks = monks + pieceOnBoard)
            Abbot -> copy(abbots = abbots + pieceOnBoard)
        }
    }

    fun remove(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard {
        val pieceOnBoard = OwnedPiece(player.id, PieceOnBoard(position, piece, role))
        return when (role) {
            is Knight -> copy(knights = knights - pieceOnBoard)
            is Brigand -> copy(brigands = brigands - pieceOnBoard)
            is Peasant -> copy(peasants = peasants - pieceOnBoard)
            Monk -> copy(monks = monks - pieceOnBoard)
            Abbot -> copy(abbots = abbots - pieceOnBoard)
        }
    }

    fun piecesOn(position: Position, role: Role): List<OwnedPiece> = when (role) {
        is Knight -> piecesOn(knights, position, role)
        is Brigand -> piecesOn(brigands, position, role)
        is Peasant -> piecesOn(peasants, position, role)
        Monk -> piecesOn(monks, position, role)
        Abbot -> piecesOn(abbots, position, role)
    }

    fun allKnights(): List<OwnedPiece> = knights

    fun allBrigands(): List<OwnedPiece> = brigands

    fun allMonks(): List<OwnedPiece> = monks

    fun allAbbots(): List<OwnedPiece> = abbots

    fun allPeasants(): List<OwnedPiece> = peasants

    private fun piecesOn(list: List<OwnedPiece>, position: Position, role: Role) =
        list.filter { it.pieceOnBoard.position == position && it.pieceOnBoard.role == role }

    fun playerPieces(player: IPlayer, symbol: CornSymbol): List<OwnedPiece> = when (symbol) {
        CornSymbol.KNIGHT -> knights.filter { it.playerId == player.id }
        CornSymbol.BRIGAND -> brigands.filter { it.playerId == player.id }
        CornSymbol.PEASANT -> peasants.filter { it.playerId == player.id }
    }

    fun putNPC(piece: NpcPiece, position: Position, direction: Direction): PiecesOnBoard =
        copy(npc = npc.filter { it.piece != piece } + NPCOnBoard(piece, position, direction))

    fun containsNPC(position: Position, direction: Direction, piece: NpcPiece): Boolean =
        npc.contains(NPCOnBoard(piece, position, direction))

    fun magician(): NPCOnBoard? = npc.find { it.piece == MagePiece }

    fun witch(): NPCOnBoard? = npc.find { it.piece == WitchPiece }
}
