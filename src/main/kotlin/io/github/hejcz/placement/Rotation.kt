package io.github.hejcz.placement

sealed class Rotation

object NoRotation : Rotation()

object Rotation90 : Rotation()

object Rotation180 : Rotation()

object Rotation270 : Rotation()