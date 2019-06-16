package io.github.hejcz.core

import io.github.hejcz.core.tile.*

interface RemainingTiles {
    fun next(): Tile
    fun size(): Int
}
