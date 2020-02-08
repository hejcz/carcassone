package io.github.hejcz.core

data class NoHandlerForCommand(val command: Command) : Throwable() {
}
