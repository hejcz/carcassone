package io.github.hejcz.mapples

import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Location

sealed class PieceRole

data class Knight(val direction: Direction) : PieceRole()

data class Brigand(val direction: Direction) : PieceRole()

data class Peasant(val location: Location) : PieceRole()

object Monk : PieceRole()