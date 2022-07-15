package de.dargmuesli.spotilist.providers.provider

import de.dargmuesli.spotilist.providers.ISpotilistProvider

object NoneProvider : ISpotilistProvider<Unit, Unit> {
    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return playlistId.isEmpty()
    }
}