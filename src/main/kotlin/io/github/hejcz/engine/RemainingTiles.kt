package io.github.hejcz.engine

import io.github.hejcz.tiles.basic.Tile

interface RemainingTiles {
    fun next(): Tile
    fun size(): Int
}