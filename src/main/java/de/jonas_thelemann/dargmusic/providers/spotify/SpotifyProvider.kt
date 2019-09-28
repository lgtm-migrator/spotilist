package de.jonas_thelemann.dargmusic.providers.spotify

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.exceptions.SpotifyWebApiException
import com.wrapper.spotify.exceptions.detailed.NotFoundException
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException
import com.wrapper.spotify.model_objects.specification.PlaylistTrack
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest
import de.jonas_thelemann.dargmusic.models.enums.AlbumType
import de.jonas_thelemann.dargmusic.models.music.Album
import de.jonas_thelemann.dargmusic.models.music.Artist
import de.jonas_thelemann.dargmusic.models.music.Playlist
import de.jonas_thelemann.dargmusic.models.music.Track
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.providers.IDargmusicProviderAuthorizable
import org.apache.logging.log4j.LogManager
import java.io.IOException


object SpotifyProvider : IDargmusicProviderAuthorizable {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
            .setClientId(DargmusicState.settings.spotifySettings.clientId)
            .setClientSecret(DargmusicState.settings.spotifySettings.clientSecret)
            .setRedirectUri(DargmusicState.settings.spotifySettings.redirectUri)
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials.accessToken != "") {
            spotifyApi = spotifyApiBuilder
                    .setAccessToken(DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials.accessToken)
                    .build()
        }
    }

    override fun getPlaylist(playlistId: String): Playlist {
        val spotifyPlaylist = spotifyApi.getPlaylist(playlistId).build().execute()
        val spotifyPlaylistTracks = SpotifyUtil.getAllPagingItems(GetPlaylistsTracksRequest.Builder(spotifyApi.accessToken))
        val playlistTracks = arrayListOf<Track>()

        spotifyPlaylistTracks.forEach { spotifyPlaylistTrack: PlaylistTrack ->
            val trackAlbumType = AlbumType.valueOf(spotifyPlaylistTrack.track.album.albumType.name)
            val trackAlbumArtists: MutableList<Artist> = mutableListOf()

            spotifyPlaylistTrack.track.album.artists.forEach { artistSimplified ->
                trackAlbumArtists.add(Artist(name = artistSimplified.name))
            }

            val trackAlbumName: String = spotifyPlaylistTrack.track.album.name
            val trackAlbum = Album(albumType = trackAlbumType, artists = trackAlbumArtists, name = trackAlbumName)
            val trackDurationMs = spotifyPlaylistTrack.track.durationMs
            val trackName = spotifyPlaylistTrack.track.name

            playlistTracks.add(Track(trackAlbum, trackAlbumArtists, trackDurationMs, trackName))
        }

        return Playlist(spotifyPlaylist.name, playlistTracks)
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
        if (DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials.accessToken == null) {
            return false
        }

        var id = String()

        try {
            id = spotifyApi.currentUsersProfile.build().execute().id
        } catch (e: UnauthorizedException) {
            LogManager.getLogger().debug("Access to the Spotify API was unauthorized. Check the access credentials!")
        }

        return id != ""
    }
}
