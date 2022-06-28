package de.dargmuesli.spotilist.providers

interface ISpotilistProviderAuthorizable : ISpotilistProvider {
    fun isAuthorized(): Boolean
}
