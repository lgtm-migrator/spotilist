package de.dargmuesli.spotilist.providers

import de.dargmuesli.spotilist.models.music.Playlist

object NoneProvider : ISpotilistProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        return Playlist()
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return true
    }
}