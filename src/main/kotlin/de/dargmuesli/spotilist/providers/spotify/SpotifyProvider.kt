package de.dargmuesli.spotilist.providers.spotify

import de.dargmuesli.spotilist.models.enums.AlbumType
import de.dargmuesli.spotilist.models.music.Album
import de.dargmuesli.spotilist.models.music.Artist
import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.models.music.Track
import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.persistence.config.SpotifyConfig
import de.dargmuesli.spotilist.providers.ISpotilistProviderAuthorizable
import org.apache.logging.log4j.LogManager
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest
import java.awt.Desktop
import java.io.IOException
import java.net.URI


object SpotifyProvider : ISpotilistProviderAuthorizable {
    private val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
        .setClientId(SpotifyConfig.clientId.value)
        .setClientSecret(SpotifyConfig.clientSecret.value)
        .setRedirectUri(URI(SpotifyConfig.redirectUri.value))
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (!SpotifyCache.accessToken.value.isNullOrEmpty()) {
            spotifyApi = spotifyApiBuilder
                .setAccessToken(SpotifyCache.accessToken.value)
                .build()
        }
    }

    fun openAuthorization() {
        val authorizationCode = spotifyApi.authorizationCodeUri()
            .build().execute()
        Desktop.getDesktop().browse(authorizationCode)

//        SpotifyCache.accessToken.value = authorizationCode.accessToken
    }

    private fun <T> getAllPagingItems(requestBuilder: AbstractDataPagingRequest.Builder<T, *>): List<T> {
        val list = arrayListOf<T>()

        do {
            val paging = requestBuilder.build().execute()
            list.addAll(paging.items)
            requestBuilder
                .offset(paging.offset + paging.limit)
        } while (paging.next != null)

        return list
    }

    override fun getPlaylist(playlistId: String): Playlist {
        val spotifyPlaylistName = spotifyApi.getPlaylist(playlistId).build().execute().name
        val spotifyPlaylistTracks = getAllPagingItems(
            spotifyApi.getPlaylistsItems(playlistId)
        )
        val playlistTracks = arrayListOf<Track>()

        spotifyPlaylistTracks.forEach { spotifyPlaylistTrack: PlaylistTrack ->
            val track = spotifyPlaylistTrack.track as se.michaelthelin.spotify.model_objects.specification.Track
            val trackAlbumType = AlbumType.valueOf(track.album.albumType.name)
            val trackAlbumArtists: MutableList<Artist> = mutableListOf()
            val trackArtists: MutableList<Artist> = mutableListOf()

            track.album.artists.forEach { artistSimplified ->
                trackAlbumArtists.add(Artist(name = artistSimplified.name))
            }

            track.artists.forEach { artistSimplified ->
                trackArtists.add(Artist(name = artistSimplified.name))
            }

            val trackAlbumName: String = track.album.name
            val trackAlbum = Album(albumType = trackAlbumType, artists = trackAlbumArtists, name = trackAlbumName)
            val trackDurationMs = spotifyPlaylistTrack.track.durationMs
            val trackName = spotifyPlaylistTrack.track.name

            playlistTracks.add(Track(trackAlbum, trackArtists, trackDurationMs.toLong(), trackName))
        }

        return Playlist(name = spotifyPlaylistName, tracks = playlistTracks)
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        val errorMessage = "Playlist validation failed!"

        if (playlistId == "") {
            return false
        }

        return try {
            spotifyApi.getPlaylist(playlistId).build().execute()
            true
        } catch (e: IOException) {
            LogManager.getLogger().error(errorMessage, e)
            false
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: SpotifyWebApiException) {
            if (e !is NotFoundException) {
                LogManager.getLogger().error("$errorMessage SpotifyWebApiException is not a NotFoundException.", e)
            }

            false
        }
    }

    override fun isAuthorized(): Boolean {
        if (SpotifyCache.accessToken.value == null) {
            return false
        }

        return true
    }
}
