package de.dargmuesli.spotilist.providers.provider

import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.providers.ISpotilistProvider

object NoneProvider : ISpotilistProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        return Playlist()
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return true
    }
}