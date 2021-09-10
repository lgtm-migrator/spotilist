package de.dargmuesli.dargmusic.providers

import de.dargmuesli.dargmusic.models.music.Playlist

object NoneProvider : IDargmusicProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        return Playlist()
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return true
    }
}