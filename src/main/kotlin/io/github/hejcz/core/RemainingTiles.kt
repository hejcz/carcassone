package io.github.hejcz.core

import io.github.hejcz.basic.tiles.*

interface RemainingTiles {
    fun next(): Tile
    fun size(): Int
}
