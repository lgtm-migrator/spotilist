package de.dargmuesli.spotilist.providers.spotify

import de.dargmuesli.spotilist.models.enums.AlbumType
import de.dargmuesli.spotilist.models.music.Album
import de.dargmuesli.spotilist.models.music.Artist
import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.models.music.Track
import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.persistence.config.SpotifyConfig
import de.dargmuesli.spotilist.providers.ISpotilistProviderAuthorizable
import de.dargmuesli.spotilist.ui.SpotilistNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.javafx.JavaFxDispatcher
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.util.*


object SpotifyProvider : ISpotilistProviderAuthorizable, CoroutineScope {
    override val coroutineContext: JavaFxDispatcher
        get() = Dispatchers.JavaFx

    private val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
    private val spotifyApi: SpotifyApi
        get() = spotifyApiBuilder.build()
    private val LOGGER = LogManager.getLogger()

    init {
        SpotifyCache.accessToken.addListener { _ ->
            createSpotifyApiBuilder()
        }

        SpotifyConfig.clientId.addListener { _ ->
            createSpotifyApiBuilder()
        }

        SpotifyConfig.clientSecret.addListener { _ ->
            createSpotifyApiBuilder()
        }

        SpotifyConfig.redirectUri.addListener { _ ->
            createSpotifyApiBuilder()
        }

        createSpotifyApiBuilder()
    }

    private fun createSpotifyApiBuilder() {
        spotifyApiBuilder
            .setClientId(SpotifyConfig.clientId.value)
            .setClientSecret(SpotifyConfig.clientSecret.value)
            .setAccessToken(SpotifyCache.accessToken.value)

        if (!SpotifyConfig.redirectUri.value.isNullOrEmpty()) {
            spotifyApiBuilder
                .setRedirectUri(URI(SpotifyConfig.redirectUri.value))
        }
    }

    fun authorize() {
        if (SpotifyConfig.authorizationCode.value.isNullOrEmpty()) {
            val authorizationCodeUri = spotifyApi.authorizationCodeUri()
                .build().execute()

            launch(Dispatchers.IO) {
                Desktop.getDesktop().browse(authorizationCodeUri)
            }
        } else {
            try {
                val authorizationCode =
                    spotifyApi.authorizationCode(SpotifyConfig.authorizationCode.value).build().execute()
                SpotifyCache.accessToken.set(authorizationCode.accessToken)
                SpotifyCache.refreshToken.set(authorizationCode.refreshToken)
                SpotifyCache.accessTokenExpiresAt.set(Date().time / 1000 + authorizationCode.expiresIn)
            } catch (e: BadRequestException) {
                e.message?.let { SpotilistNotification.error(it) }
            }
        }
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

            if (track.linkedFrom != null) {
                LOGGER.warn(track.name + "might differ! " + track.linkedFrom.id)
            }

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

        if (playlistId == "" || !isAuthorized()) {
            return false
        }

        return try {
            spotifyApi.getPlaylist(playlistId).build().execute()
            true
        } catch (e: IOException) {
            LOGGER.error(errorMessage, e)
            false
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: SpotifyWebApiException) {
            if (e !is NotFoundException) {
                LOGGER.error("$errorMessage SpotifyWebApiException is not a NotFoundException.", e)
            }

            false
        }
    }

    override fun isAuthorized(): Boolean {
        if (SpotifyCache.accessTokenExpiresAt.value > 0 && SpotifyCache.accessTokenExpiresAt.value > Date().time / 1000) {
            return true
        }

        return false
    }
}
