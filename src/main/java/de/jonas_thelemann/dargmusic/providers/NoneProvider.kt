package de.jonas_thelemann.dargmusic.providers

import de.jonas_thelemann.dargmusic.models.music.Playlist

object NoneProvider : IDargmusicProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        return Playlist()
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return true
    }
}