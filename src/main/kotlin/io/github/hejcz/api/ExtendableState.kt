package io.github.hejcz.api

interface ExtendableState {
    fun update(extension: StateExtension): State
    fun get(id: StateExtensionId): StateExtension?
}