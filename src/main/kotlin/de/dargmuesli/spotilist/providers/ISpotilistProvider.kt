package de.dargmuesli.spotilist.providers

import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.models.music.Track

interface ISpotilistProvider<P, T> {
    fun getProviderPlaylist(playlistId: String): P? {
        return null
    }

    fun getProviderPlaylistItems(playlistId: String): List<T>? {
        return null
    }

    fun getPlaylist(playlistId: String): Playlist? {
        return null
    }

    fun getPlaylistItems(playlistId: String): List<Track>? {
        return null
    }

    fun isPlaylistIdValid(playlistId: String): Boolean {
        return getPlaylist(playlistId) != null
    }
}
