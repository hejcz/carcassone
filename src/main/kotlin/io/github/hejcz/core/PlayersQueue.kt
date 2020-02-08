package io.github.hejcz.core

import java.lang.RuntimeException

interface IPlayersQueue {
    fun next(): IPlayersQueue
    fun current(): IPlayer
}

/**
 * Cycles over passed players infinitely.
 */
class PlayersQueue(players: Collection<Player>) : IPlayersQueue {
    private var current: Player? = null

    private val iterator = sequence {
        val orderedPlayers = players.sortedBy { it.order }
        var i = 0
        while (true) {
            yield(orderedPlayers[i])
            i = (i + 1) % orderedPlayers.size
        }
    }.iterator()

    override fun next(): PlayersQueue {
        current = iterator.next()
        return this
    }

    override fun current(): IPlayer = current ?: throw RuntimeException("queue not executed yet")
}
