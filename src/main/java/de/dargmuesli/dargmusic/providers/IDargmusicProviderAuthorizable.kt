package de.dargmuesli.dargmusic.providers

interface IDargmusicProviderAuthorizable : IDargmusicProvider {
    fun isAuthorized(): Boolean
}
