package io.github.hejcz.api

interface Role {
    fun canBePlacedOn(tile: Tile): Boolean
}