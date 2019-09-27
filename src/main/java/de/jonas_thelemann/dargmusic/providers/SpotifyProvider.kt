package de.jonas_thelemann.dargmusic.providers

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.exceptions.SpotifyWebApiException
import com.wrapper.spotify.exceptions.detailed.NotFoundException
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException
import com.wrapper.spotify.model_objects.specification.Playlist
import com.wrapper.spotify.model_objects.specification.PlaylistTrack
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import org.apache.logging.log4j.LogManager
import java.awt.Desktop
import java.io.IOException


object SpotifyProvider : AbstractDargmusicProvider<Playlist, PlaylistTrack>() {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
            .setClientId(DargmusicState.settings.spotifySettings.clientId)
            .setClientSecret(DargmusicState.settings.spotifySettings.clientSecret)
            .setRedirectUri(DargmusicState.settings.spotifySettings.redirectUri)
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (DargmusicState.data.spotifyData.accessToken != "") {
            spotifyApi = spotifyApiBuilder
                    .setAccessToken(DargmusicState.data.spotifyData.accessToken)
                    .build()
        }
    }

    override fun getPlaylistId(playlist: Playlist): String {
        return playlist.id
    }

    override fun getPlaylistName(playlist: Playlist): String {
        return playlist.name
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

    fun isValid(): Boolean {
        if (DargmusicState.data.spotifyData.accessToken == "") {
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

    fun openAuthorization() {
        val uri = spotifyApi.authorizationCodeUri()
                .redirect_uri(DargmusicState.settings.spotifySettings.redirectUri)
//                .scope("playlist-modify-private playlist-read-private user-read-private")
                .state("dargmusic")
                .build().execute()

        val os = System.getProperty("os.name").toLowerCase()
        val runtime = Runtime.getRuntime()

        if (os.contains("mac")) {
            runtime.exec("open $uri")
        } else if (os.contains("nix") || os.contains("nux")) {
            runtime.exec("xdg-open $uri")
        } else if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }

    fun setAuthorizationCodeCredentials(authorizationCode: String) {
        if (authorizationCode == "") {
            return
        }

        val authorizationCodeCredentials = spotifyApi.authorizationCode(authorizationCode)
                .build().execute()

        DargmusicState.data.spotifyData.accessToken = authorizationCodeCredentials.accessToken
        DargmusicState.data.spotifyData.refreshToken = authorizationCodeCredentials.refreshToken
    }
}
