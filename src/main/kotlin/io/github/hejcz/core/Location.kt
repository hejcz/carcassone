package io.github.hejcz.core

typealias Locations = Collection<Location>

data class Location(val direction: Direction, val side: Side?) {
    constructor(direction: Direction) : this(direction, null)

    fun left(): Location = Location(direction.left(), side)
    fun right(): Location = Location(direction.right(), side)
    fun mirrored(): Location = Location(direction.opposite(), side?.opposite())
    fun opposite(): Location = Location(direction.opposite(), side)
}
