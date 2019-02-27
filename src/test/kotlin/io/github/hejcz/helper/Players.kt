package io.github.hejcz.helper

import io.github.hejcz.engine.Player

object Players {
    fun singlePlayer() = setOf(Player(id = 1, order = 1))
    fun twoPlayers() = setOf(Player(id = 1, order = 1), Player(id = 2, order = 2))
}
