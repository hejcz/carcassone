package io.github.hejcz.api

interface Piece {
    fun mayBeUsedAs(role: Role): Boolean
    fun power(): Int
}