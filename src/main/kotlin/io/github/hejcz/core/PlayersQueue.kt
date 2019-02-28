package io.github.hejcz.core

/**
 * Cycles over passed players infinitely.
 */
class PlayersQueue(players: Collection<Player>) {
    private val iterator = sequence {
        val orderedPlayers = players.sortedBy { it.order }
        var i = 0
        while (true) {
            yield(orderedPlayers[i])
            i = (i + 1) % orderedPlayers.size
        }
    }.iterator()

    fun next() = iterator.next()
}
