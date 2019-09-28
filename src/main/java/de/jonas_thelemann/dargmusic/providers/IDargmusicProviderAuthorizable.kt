package de.jonas_thelemann.dargmusic.providers

interface IDargmusicProviderAuthorizable : IDargmusicProvider {
    fun isAuthorized(): Boolean
}
