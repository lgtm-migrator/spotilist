package de.dargmuesli.spotilist.providers

import de.dargmuesli.spotilist.models.music.Playlist

interface ISpotilistProvider {
    fun getPlaylist(playlistId: String): Playlist
    fun isPlaylistIdValid(playlistId: String): Boolean
}
