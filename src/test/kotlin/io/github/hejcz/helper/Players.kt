package io.github.hejcz.helper

import io.github.hejcz.base.Player
import io.github.hejcz.setup.PiecesSetup

object Players {
    fun singlePlayer() = setOf(Player(id = 1, order = 1, initialPieces = PiecesSetup().pieces()))
    fun twoPlayers() = setOf(
        Player(id = 1, order = 1, initialPieces = PiecesSetup().pieces()),
        Player(id = 2, order = 2, initialPieces = PiecesSetup().pieces())
    )
}
