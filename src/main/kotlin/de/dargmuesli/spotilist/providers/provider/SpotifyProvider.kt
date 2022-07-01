package de.dargmuesli.spotilist.providers.provider

import de.dargmuesli.spotilist.models.music.Album
import de.dargmuesli.spotilist.models.music.Artist
import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.models.music.Track
import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.providers.ISpotilistProviderAuthorizable
import de.dargmuesli.spotilist.providers.util.SpotifyUtil.getAllPagingItems
import de.dargmuesli.spotilist.providers.util.SpotifyUtil.spotifyApi
import org.apache.logging.log4j.LogManager
import se.michaelthelin.spotify.enums.AlbumType
import java.util.*


object SpotifyProvider :
    ISpotilistProviderAuthorizable<se.michaelthelin.spotify.model_objects.specification.Playlist, se.michaelthelin.spotify.model_objects.specification.PlaylistTrack> {
    private val LOGGER = LogManager.getLogger()

    override fun getProviderPlaylist(playlistId: String): se.michaelthelin.spotify.model_objects.specification.Playlist? {
        return if (SpotifyCache.playlistData.containsKey(playlistId)) {
            SpotifyCache.playlistData[playlistId]
        } else {
            spotifyApi.getPlaylist(playlistId).build().execute().also {
                SpotifyCache.playlistData[it.id] = it
            }
        }
    }

    override fun getProviderPlaylistItems(playlistId: String): List<se.michaelthelin.spotify.model_objects.specification.PlaylistTrack>? {
        // TODO: Is there a meaningful way to use the cache here?
        return getAllPagingItems(spotifyApi.getPlaylistsItems(playlistId))
            .ifEmpty { null }
            ?.onEach {
                SpotifyCache.playlistItemData[it.track.id] = it
            }
    }

    override fun getPlaylist(playlistId: String): Playlist? {
        val playlist = getProviderPlaylist(playlistId) ?: return null
        return Playlist(name = playlist.name, tracks = getPlaylistItems(playlistId))
    }

    override fun getPlaylistItems(playlistId: String): List<Track>? {
        val spotifyPlaylistItems = getProviderPlaylistItems(playlistId) ?: return null

        return spotifyPlaylistItems.map { spotifyPlaylistTrack: se.michaelthelin.spotify.model_objects.specification.PlaylistTrack ->
            val track = spotifyPlaylistTrack.track as se.michaelthelin.spotify.model_objects.specification.Track
            val trackAlbumType = AlbumType.valueOf(track.album.albumType.name)
            val trackAlbumArtists = mutableListOf<Artist>()
            val trackArtists = mutableListOf<Artist>()

            if (track.linkedFrom != null) {
                LOGGER.warn(track.name + "might differ! " + track.linkedFrom.id)
            }

            track.album.artists.forEach { artistSimplified ->
                trackAlbumArtists.add(Artist(name = artistSimplified.name))
            }

            track.artists.forEach { artistSimplified ->
                trackArtists.add(Artist(name = artistSimplified.name))
            }

            val trackAlbumName = track.album.name
            val trackAlbum = Album(albumType = trackAlbumType, artists = trackAlbumArtists, name = trackAlbumName)
            val trackDurationMs = spotifyPlaylistTrack.track.durationMs
            val trackName = spotifyPlaylistTrack.track.name

            Track(trackAlbum, trackArtists, trackDurationMs.toLong(), trackName)
        }
    }

    override fun isAuthorized(): Boolean {
        if (SpotifyCache.accessTokenExpiresAt.value > 0 && SpotifyCache.accessTokenExpiresAt.value > Date().time / 1000) {
            return true
        }

        return false
    }
}
