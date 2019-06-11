package io.github.hejcz.core

import io.github.hejcz.basic.tile.*

interface RemainingTiles {
    fun next(): Tile
    fun size(): Int
}
