package de.jonas_thelemann.dargmusic.providers

import de.jonas_thelemann.dargmusic.models.music.Playlist

interface IDargmusicProvider {
    fun getPlaylist(playlistId: String): Playlist
    fun isPlaylistIdValid(playlistId: String): Boolean
}
