package de.dargmuesli.spotilist.providers

interface ISpotilistProviderAuthorizable<P, T> : ISpotilistProvider<P, T> {
    fun isAuthorized(): Boolean
}
