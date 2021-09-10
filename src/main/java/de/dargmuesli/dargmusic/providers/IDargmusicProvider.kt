package de.dargmuesli.dargmusic.providers

import de.dargmuesli.dargmusic.models.music.Playlist

interface IDargmusicProvider {
    fun getPlaylist(playlistId: String): Playlist
    fun isPlaylistIdValid(playlistId: String): Boolean
}
